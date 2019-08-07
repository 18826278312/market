var companyList=[];
var businessList=[];
var dutyList=[];
var regionList=[];
var polygons = [];
var polygons2 = [];
var regionPolygons = "";
var polygonEditor = null;
var editStatus = false;
var addStatus =false;
var regionId="";
var gridList = [];
var gridElements = [];
var lng = "";
var lat = "";
var rightRegion = null;
var rightDuty = null;
var addressList = [];
var elements = [];
var gridStatus = true;
var gridPage = 0;
var power = null;
var allRegion = [];
var map = new AMap.Map('map',{
	center: [116.706691,23.299111]
});

//用于谷歌卫星地图切换加载瓦片图层展示
var googleLayer = new AMap.TileLayer({
	getTileUrl: 'http://mt{1,2,3,0}.google.cn/vt/lyrs=s&hl=zh-CN&gl=cn&x=[x]&y=[y]&z=[z]&s=Galile'
});//定义谷歌卫星切片图层
var roadNetLayer = new AMap.TileLayer.RoadNet(); //定义一个路网图层
map.on('rightclick', rightClick);
//初始化地图
drawBounds();
//监听draw事件可获取画好的覆盖物
var mouseTool = new AMap.MouseTool(map); 
var overlay = null;
mouseTool.on('draw',draw1) 
function draw1(e){
	mouseTool.close();
	overlay=e;
}

//获取网格数据
$.post("./getLists",{
},function(data){
	if(data.status==0){
		power = data.power;
		companyList = data.companyList;
		businessList = data.businessList;
		dutyList = data.dutyList;
	}else{
		//alert(data.info);
	}
},"json");

//选择分公司
$("#area").change(function(){
	empty();
	rightRegion=null;
	rightDuty = null;
	$("#part").empty();
	$("#part").append('<option value="">-业务网格-</option>');
	$("#town").empty();
	$("#town").append('<option value="">-责任田-</option>');
	$("#region").empty();
	$("#region").append('<option value="">显示全部</option>');
	var area = $("#area").val();
	if(area==""){
		drawBounds();
	}else{
		for(var i=0;i<businessList.length;i++){
			if(area==businessList[i].companyName){
				$("#part").append('<option value="'+businessList[i].businessName+'">'+businessList[i].businessName+'</option>');
			}
		}
		for(var i=0;i<companyList.length;i++){
			if(area==companyList[i].companyName){
				var array = companyList[i].gaode.split("|");
				var lnglats = array[0].split(";");
				draw(lnglats);
				if(array.length==2){
					lnglats = array[1].split(";");
					var path = [];
					for(var j=0;j<lnglats.length;j++){
						var lnglat = [];
						lnglat.push(lnglats[j].split(",")[0]);
						lnglat.push(lnglats[j].split(",")[1]);
						path.push(lnglat);
					}
					polygons2 = new AMap.Polygon({
				        path: path,
				        strokeColor: "red", 
				        strokeWeight: 4,
				        strokeOpacity: 1,
				        fillOpacity: 0,
				        zIndex: 45,
				    })
					map.add(polygons2);
				}
			}
		}
	}
})

//选择业务网格
$("#part").change(function(){
	empty();
	rightRegion=null;
	rightDuty = null;
	$("#town").empty();
	$("#town").append('<option value="">-责任田-</option>');
	$("#region").empty();
	$("#region").append('<option value="">显示全部</option>');
	var part = $("#part").val();
	if(part==""){
		var area = $("#area").val();
		for(var i=0;i<companyList.length;i++){
			if(area==companyList[i].companyName){
				var array = companyList[i].gaode.split("|");
				var lnglats = array[0].split(";");
				draw(lnglats);
				if(array.length==2){
					lnglats = array[1].split(";");
					var path = [];
					for(var j=0;j<lnglats.length;j++){
						var lnglat = [];
						lnglat.push(lnglats[j].split(",")[0]);
						lnglat.push(lnglats[j].split(",")[1]);
						path.push(lnglat);
					}
					polygons2 = new AMap.Polygon({
				        path: path,
				        strokeColor: "red", 
				        strokeWeight: 4,
				        strokeOpacity: 1,
				        fillOpacity: 0,
				        zIndex: 45,
				    })
					map.add(polygons2);
					polygons2.on('rightclick', rightClick);
				}
			}
		}
	}else{
		for(var i=0;i<dutyList.length;i++){
			if(part==dutyList[i].businessName){
				$("#town").append('<option value="'+dutyList[i].dutyName+'">'+dutyList[i].dutyName+'</option>');
			}
		}
		for(var i=0;i<businessList.length;i++){
			if(part==businessList[i].businessName){
				var lnglats = businessList[i].gaode.split(";");
				draw(lnglats);
			}
		}
	}
})

//选择责任田
$("#town").change(function(){
	$("#regionNameInfo").html("");
	$("#dutyUserInfo").html("");
	$("#portalInfo").html("");
	$("#telephoneInfo").html("");
	if(allRegion != []){
		map.remove(allRegion);
		allRegion = "";
	}
	$('input[name="scenesLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	$('input[name="businessLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	$('input[name="featureLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	changeTown();
})

//选择责任田
function changeTown(){
	if(regionPolygons!=""){
		map.remove(regionPolygons);
		regionPolygons = "";
	}
	if(gridElements!=""){
		map.remove(gridElements);
		gridElements = [];
	}
	rightRegion=null;
	rightDuty = null;
	$("#region").empty();
	$("#region").append('<option value="">显示全部</option>');
	var town = $("#town").val();
	if(town==""){
		empty();
		rightRegion=null;
		rightDuty = null;
		$('#gridTable tr td').remove();
		$("#previous").attr('disabled',true);
		$("#next").attr('disabled',true);
		$("#gridButton").attr('disabled',true);
		$("#gridButton").html("显示微网格");
		gridStatus = true;
		var part = $("#part").val();
		for(var i=0;i<businessList.length;i++){
			if(part==businessList[i].businessName){
				var lnglats = businessList[i].gaode.split(";");
				draw(lnglats);
			}
		}
		$("#add").attr('disabled',true);
		$("#edit").attr('disabled',true);  
		$("#remove").attr('disabled',true);  
		$("#save").attr('disabled',true); 
		$("#cancel").attr('disabled',true);
	}else{
		var companyName = $("#area").val();
		var businessName = $("#part").val();
		var dutyName = $("#town").val();
		getRegion();
		var duty = null;
		for(var i=0;i<dutyList.length;i++){
			if(town==dutyList[i].dutyName){
				var lnglats = dutyList[i].gaode.split(";");
				duty = dutyList[i];
				//获取网格数据
				$.post("./getGrid",{
					"businessName":dutyList[i].businessName,
					"lnglat":dutyList[i].gaode
				},function(data){
					if(data.status==0){
						gridList=data.gridList;
						gridPage = 0;
						setGridTable();
						drawGridList();
						draw(lnglats);
						$("#gridButton").attr('disabled',false);
						$("#gridButton").html("隐藏微网格");
						gridStatus = true;
					}else{
						alert(data.info);
					}
				},"json");
				break;
			}
		}
		if(judgePower(duty)){
			$("#add").attr('disabled',false);
			$("#edit").attr('disabled',true);  
			$("#remove").attr('disabled',true);  
			$("#save").attr('disabled',true); 
			$("#cancel").attr('disabled',true);
			$("#power").html("");
		}else{
			$("#power").html("该责任田没有操作权限");
		}
	}
}

//选择微区域
$("#region").change(function(){
	changeRegion();
})

//选择微区域
function changeRegion(){
	if(allRegion != []){
		map.remove(allRegion);
		allRegion = "";
	}
	if(regionPolygons!=""){
		map.remove(regionPolygons);
		regionPolygons = "";
	}
	rightRegion=null;
	rightDuty = null;
	var region = $("#region").val();
	//如果选择具体微区域，则直接绘制该微区域。否则绘制所有微区域
	if(region!=""){
		drawRegion();
	}else{
		$("#regionName").val("");
		$("#dutyUser").val("");
		$('input[name="scenesLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
		    $(this).removeAttr("checked");
		});
		$('input[name="businessLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
		    $(this).removeAttr("checked");
		});
		$('input[name="featureLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
		    $(this).removeAttr("checked");
		});
		$("#portal").val("");
		$("#telephone").val("");
		//changeTown();
		drawAllRegion();
		var town = $("#town").val();
		for(var i=0;i<dutyList.length;i++){
			if(town==dutyList[i].dutyName){
				duty = dutyList[i];
			}
		}
		if(judgePower(duty)){
			$("#add").attr('disabled',false);
			$("#edit").attr('disabled',true);  
			$("#remove").attr('disabled',true);  
			$("#save").attr('disabled',true); 
			$("#cancel").attr('disabled',true);
			$("#power").html("");
		}else{
			$("#power").html("该责任田没有操作权限");
		}
	}
}

//绘制所有微区域
function drawAllRegion(){
	allRegion = [];
	for(var i=0;i<regionList.length;i++){
		var lnglats = regionList[i].polygonGaode.split(";");
		var path = [];
		for(var j=0;j<lnglats.length;j++){
			var lnglat = [];
			lnglat.push(lnglats[j].split(",")[0]);
			lnglat.push(lnglats[j].split(",")[1]);
			path.push(lnglat);
		}
		var region = new AMap.Polygon({
	        path: path,
	        strokeColor: "#5BBFDF", 
	        strokeWeight: 4,
	        strokeOpacity: 1,
	        fillOpacity: 0,
	        zIndex: 45,
	    })
		region.on('rightclick', rightClick);
		allRegion.push(region);
	}
	map.add(allRegion);
}

//绘制选择的微区域
function drawRegion(rightRegion){
	$('input[name="scenesLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	$('input[name="businessLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	$('input[name="featureLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	var region = null;
	if(rightRegion!=null){
		region = rightRegion;
	}else{
		region = $("#region").val();
	}
	var duty = null;
	for(var i=0;i<regionList.length;i++){
		if(region==regionList[i].regionName){
			duty = regionList[i];
			var lnglats = regionList[i].polygonGaode.split(";");
			$("#regionName").val(regionList[i].regionName);
			$("#dutyUser").val(regionList[i].dutyUser);
			var scenesLabel = regionList[i].scenesLabel.split(",");
			$('input[name="scenesLabel"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
				for(var j=0;j<scenesLabel.length;j++){
					if($(this).val()==scenesLabel[j]){
						$(this).prop("checked",true);
					}
				}
			});
			var businessLabel = regionList[i].businessLabel.split(",");
			$('input[name="businessLabel"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
				for(var j=0;j<businessLabel.length;j++){
					if($(this).val()==businessLabel[j]){
						$(this).prop("checked",true);
					}
				}
			});
			var featureLabel = regionList[i].featureLabel.split(",");
			$('input[name="featureLabel"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
				for(var j=0;j<featureLabel.length;j++){
					if($(this).val()==featureLabel[j]){
						$(this).prop("checked",true);
					}
				}
			});
			$("#portal").val(regionList[i].portal);
			$("#telephone").val(regionList[i].telephone);
			regionId = regionList[i].id;
			//绘制微区域
			var path = [];
			for(var j=0;j<lnglats.length;j++){
				var lnglat = [];
				lnglat.push(lnglats[j].split(",")[0]);
				lnglat.push(lnglats[j].split(",")[1]);
				path.push(lnglat);
			}
			regionPolygons = new AMap.Polygon({
		        path: path,
		        strokeColor: "orange", 
		        strokeWeight: 4,
		        strokeOpacity: 1,
		        fillOpacity: 0,
		        zIndex: 45,
		    })
			regionPolygons.on('rightclick', rightClick);
			map.add(regionPolygons);
			//构造折线编辑对象，并开启折线的编辑状态
			map.plugin(["AMap.PolyEditor"],function(){
				polygonEditor = new AMap.PolyEditor(map,regionPolygons);
				polygonEditor.on('end', function(event) {
					if(editStatus){
						var regionName = $("#regionName").val();
						var dutyUser = $("#dutyUser").val();
						var portal = $("#portal").val();
						var telephone = $("#telephone").val();
						var companyName = $("#area").val();
						var businessName = $("#part").val();
						var dutyName = $("#town").val();
						var scenesLabel = "";
						$('input[name="scenesLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
							scenesLabel = scenesLabel + "," + $(this).val();
						});
						var businessLabel = "";
						$('input[name="businessLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
							businessLabel = businessLabel + "," + $(this).val();
						});
						var featureLabel = "";
						$('input[name="featureLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
							featureLabel = featureLabel + "," + $(this).val();
						});
						editStatus = false;
						var lnglat = ""+event.target;
						$.post("./edit",{
							"regionId":regionId,
							"lnglat":lnglat,
							"companyName":companyName,
							"businessName":businessName,
							"dutyName":dutyName,
							"regionName":regionName,
							"dutyUser":dutyUser,
							"portal":portal,
							"telephone":telephone,
							"scenesLabel":scenesLabel,
							"businessLabel":businessLabel,
							"featureLabel":featureLabel
						},function(data){
							if(data.status==0){
								alert("编辑成功");
								getRegion(regionName,true);
							}
						},"json")
					}
				})
		    });
		}
	}
	if(judgePower(duty)){
		$("#add").attr('disabled',true);
		$("#edit").attr('disabled',false);  
		$("#remove").attr('disabled',false);  
		$("#save").attr('disabled',true); 
		$("#cancel").attr('disabled',true);
		$("#power").html("");
	}else{
		$("#power").html("该责任田没有操作权限");
	}
}

//判断是否有操作权限
function judgePower(duty){
	var companyPower = power.companyPower.split(",");
	var businessPower = power.businessPower.split(",");
	var dutyPower = power.dutyPower.split(",");
	for(var i=0;i<companyPower.length;i++){
		if(companyPower[i]==duty.companyName){
			return true;
		}
	}
	for(var i=0;i<businessPower.length;i++){
		if(businessPower[i]==duty.businessName){
			return true;
		}
	}
	for(var i=0;i<dutyPower.length;i++){
		if(dutyPower[i]==duty.dutyName){
			return true;
		}
	}
	return false;
}

//绘制责任田中的细粒度网格
function drawGridList(){
	for(var i=0;i<gridList.length;i++){
		var path = [];
		var lnglats = gridList[i].split("|")[8].split(";");
		var lnglat = [];
		for(var j=0;j<lnglats.length;j++){
			var lnglat = [];
			lnglat.push(lnglats[j].split(",")[0]);
			lnglat.push(lnglats[j].split(",")[1]);
			path.push(lnglat);
		}
		var polygon = new AMap.Polygon({
	        path: path,
	        strokeColor: "orange", 
	        strokeWeight: 1,
	        strokeOpacity: 1,
	        fillOpacity: 0,
	        zIndex: 45,
	    })
		polygon.on('rightclick', rightClick);
		gridElements.push(polygon);
	}
	map.add(gridElements);
}

//显示、隐藏责任田中的微网格
$("#gridButton").click(function(){
	if(gridStatus){
		$("#gridButton").html("显示微网格");
		gridStatus = false;
		if(gridElements!=""){
			map.remove(gridElements);
			gridElements = [];
		}
	}else{
		$("#gridButton").html("隐藏微网格");
		gridStatus = true;
		drawGridList();
	}
})

//设置微网格列表
function setGridTable(){
	$('#gridTable tr td').remove();
	var length = (gridPage+1)*10 < gridList.length ? (gridPage+1)*10 : gridList.length;
	var index = 1;
	for(var i=gridPage*10;i<length;i++){
		index++;
		var vTr= "<tr><td>"+ gridList[i].split("|")[19] +"</td></tr>";
		$("#gridTable").append(vTr);
	}
	var num = parseInt(gridList.length/11)+1;
	var currentPage = gridPage+1;
	if(currentPage==1){
		$("#previous").attr('disabled',true);
	}else{
		$("#previous").attr('disabled',false);
	}
	if(currentPage==num){
		$("#next").attr('disabled',true);
	}else{
		$("#next").attr('disabled',false);
	}
}

//下一页
$("#next").click(function(){
	gridPage = gridPage+1;
	setGridTable();
})

//上一页
$("#previous").click(function(){
	gridPage = gridPage-1;
	setGridTable();
})

//右击地图进行定位
function rightClick(e){
	lng = e.lnglat.getLng();
	lat = e.lnglat.getLat();
	var marker = new AMap.Marker({
	    position: [lng,lat]
	});
	map.setFitView(marker);//视口自适应
	$.post("./getGridByLocation",{
		"lng":lng,
		"lat":lat
	},function(data){
		if(data.status==0){
			rightRegion = data.region;
			rightDuty = data.duty;
			setRightRegion();
			$("#rightClick").html(data.rightAddress);
		}else{
			//alert(data.info);
		}
	},"json");
}

//右击地图后绘制地图的责任田或微区域
function setRightRegion(){
	empty();
	$("#part").empty();
	$("#part").append('<option value="">-业务网格-</option>');
	$("#town").empty();
	$("#town").append('<option value="">-责任田-</option>');
	$("#region").empty();
	$("#region").append('<option value="">显示全部</option>');
	if(rightRegion!=null){
		$("#area").val(rightRegion.companyName);
		var area = rightRegion.companyName;
		for(var i=0;i<businessList.length;i++){
			if(area==businessList[i].companyName){
				$("#part").append('<option value="'+businessList[i].businessName+'">'+businessList[i].businessName+'</option>');
			}
		}
		$("#part").val(rightRegion.businessName);
		var part = rightRegion.businessName;
		for(var i=0;i<dutyList.length;i++){
			if(part==dutyList[i].businessName){
				$("#town").append('<option value="'+dutyList[i].dutyName+'">'+dutyList[i].dutyName+'</option>');
			}
		}
		$("#town").val(rightRegion.dutyName);
		var town = rightRegion.dutyName;
		getRegion(rightRegion.regionName);
		for(var i=0;i<dutyList.length;i++){
			if(town==dutyList[i].dutyName){
				var lnglats = dutyList[i].gaode.split(";");
				//获取网格数据
				$.post("./getGrid",{
					"businessName":dutyList[i].businessName,
					"lnglat":dutyList[i].gaode
				},function(data){
					if(data.status==0){
						gridList=data.gridList;
						gridPage = 0;
						setGridTable();
						drawGridList();
						draw(lnglats,true);
						$("#gridButton").attr('disabled',false);
						$("#gridButton").html("隐藏微网格");
						gridStatus = true;
					}else{
						//alert(data.info);
					}
				},"json");
				break;
			}
		}
		drawRegion(rightRegion.regionName);
	}else if(rightDuty!=null){
		$("#area").val(rightDuty.companyName);
		var area = rightDuty.companyName;
		for(var i=0;i<businessList.length;i++){
			if(area==businessList[i].companyName){
				$("#part").append('<option value="'+businessList[i].businessName+'">'+businessList[i].businessName+'</option>');
			}
		}
		$("#part").val(rightDuty.businessName);
		var part = rightDuty.businessName;
		for(var i=0;i<dutyList.length;i++){
			if(part==dutyList[i].businessName){
				$("#town").append('<option value="'+dutyList[i].dutyName+'">'+dutyList[i].dutyName+'</option>');
			}
		}
		$("#town").val(rightDuty.dutyName);
		var town = rightDuty.dutyName;
		getRegion();
		var duty = null;
		for(var i=0;i<dutyList.length;i++){
			if(town==dutyList[i].dutyName){
				var lnglats = dutyList[i].gaode.split(";");
				duty = dutyList[i];
				//获取网格数据
				$.post("./getGrid",{
					"businessName":dutyList[i].businessName,
					"lnglat":dutyList[i].gaode
				},function(data){
					if(data.status==0){
						gridList=data.gridList;
						gridPage = 0;
						setGridTable();
						drawGridList();
						draw(lnglats,true);
						$("#gridButton").attr('disabled',false);
						$("#gridButton").html("隐藏微网格");
						gridStatus = true;
					}else{
						//alert(data.info);
					}
				},"json");
				break;
			}
		}
		if(judgePower(rightDuty)){
			$("#add").attr('disabled',false);
			$("#edit").attr('disabled',true);  
			$("#remove").attr('disabled',true);  
			$("#save").attr('disabled',true); 
			$("#cancel").attr('disabled',true);
			$("#power").html("");
		}else{
			$("#power").html("该责任田没有操作权限");
		}
	}else{
		alert("右击地址不存在责任田");
	}
}

//点击搜索按钮
$("#submitAddress").click(function(){
	var address = $("#addressName").val();
	if(address==""){
		alert("请输入搜索地点");
	}else{
		$("#addressList").empty();
		$.post("./getPois",{
			"name":address
		},function(data){
			if(data.status=="0"){
				$("#cancelAddress").show();
				addressList = data.list;
				setAddressList();
			}else{
				//alert(data.info);
			}
		},"json");
	}
})

//设置搜索地址列表和打点
function setAddressList(){
	$("#addressList").empty();
	if(elements!=""){
		map.remove(elements);
		elements = [];
	}
	for(var i=0;i<addressList.length;i++){
		var num=i+1;
		$("#addressList").append('<a href="#" class="list-group-item" onclick="clickAddress(' + addressList[i].location.split(",")[0] + ',' + addressList[i].location.split(",")[1] + ')">' + num  + '、' + addressList[i].adname + addressList[i].address + addressList[i].name + '</a>');
		var marker = new AMap.Marker({
			offset: new AMap.Pixel(-16.5,-41),
		    icon: "../images/blue_50.png",
		    position: [addressList[i].location.split(",")[0],addressList[i].location.split(",")[1]]
		});
		if(num<10){
			marker.setLabel({
		        offset: new AMap.Pixel(8,10),  //设置文本标注偏移量
		        content: num, //设置文本标注内容
		        direction: 'center' //设置文本标注方位
			});
		}else{
			marker.setLabel({
		        offset: new AMap.Pixel(4,10),  //设置文本标注偏移量
		        content: num, //设置文本标注内容
		        direction: 'center' //设置文本标注方位
			});
		}
		marker.location = addressList[i].location;
		marker.on('click', function(e) {
			clickAddress(e.target.location.split(",")[0],e.target.location.split(",")[1]);
        });
		map.add(marker);
		elements.push(marker);
	}
	map.setFitView(elements);//视口自适应
}

//点击搜索地址的列表
function clickAddress(lng,lat){
	var marker = new AMap.Marker({
	    position: [lng,lat]
	});
	map.setFitView(marker);//视口自适应
	$.post("./getGridByLocation",{
		"lng":lng,
		"lat":lat
	},function(data){
		if(data.status==0){
			rightRegion = data.region;
			rightDuty = data.duty;
			setRightRegion();
			$("#rightClick").html(data.rightAddress);
		}else{
			//alert(data.info);
		}
	},"json");
}

//点击收起按钮
$("#cancelAddress").click(function(){
	$("#cancelAddress").hide();
	$("#addressList").empty();
	if(elements!=null){
		map.remove(elements);
	}
	elements = [];
	addressList = [];
})

//新增按钮
function add(){
	undisable();
	editStatus = false;
	addStatus = true;
	$("#edit").attr('disabled',true);  
	$("#remove").attr('disabled',true);  
	$("#add").attr('disabled',true);  
	$("#save").attr('disabled',false); 
	$("#cancel").attr('disabled',false);
	mouseTool.polygon({
    	strokeColor: "red", 
        strokeWeight: 4,
        strokeOpacity: 1,
        fillOpacity: 0,
        zIndex: 60,
    });
}

//取消按钮
function cancel(){
	disable();
	$("#regionNameInfo").html("");
	$("#dutyUserInfo").html("");
	$("#portalInfo").html("");
	$("#telephoneInfo").html("");
	if(editStatus){
		editStatus = false;
		polygonEditor.close(); 
		$("#save").attr('disabled',true); 
		$("#cancel").attr('disabled',true);
		$("#edit").attr('disabled',true);  
		$("#remove").attr('disabled',true);
		$("#add").attr('disabled',false);  
		changeRegion();
	}
	if(addStatus){
		addStatus = false;
		mouseTool.close();
		if(overlay!=null){
			map.remove(overlay.obj);
			overlay=null;
		}
		$("#save").attr('disabled',true); 
		$("#cancel").attr('disabled',true);
		$("#edit").attr('disabled',true);  
		$("#remove").attr('disabled',true); 
		$("#add").attr('disabled',false);  
	}
}

//保存按钮
function save(){
	var regionName = $("#regionName").val();
	var dutyUser = $("#dutyUser").val();
	var portal = $("#portal").val();
	var telephone = $("#telephone").val();
	var scenesLabel = "";
	$('input[name="scenesLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
		scenesLabel = scenesLabel + "," + $(this).val();
	});
	var businessLabel = "";
	$('input[name="businessLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
		businessLabel = businessLabel + "," + $(this).val();
	});
	var featureLabel = "";
	$('input[name="featureLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
		featureLabel = featureLabel + "," + $(this).val();
	});
	var status = false;
	if(regionName==""){
		status = true;
		$("#regionNameInfo").html("不能为空");
	}else{
		$("#regionNameInfo").html("");
	}
	if(dutyUser==""){
		status = true;
		$("#dutyUserInfo").html("不能为空");
	}else{
		$("#dutyUserInfo").html("");
	}
	if(portal==""){
		status = true;
		$("#portalInfo").html("不能为空");
	}else{
		$("#portalInfo").html("")
	}
	if(telephone==""){
		status = true;
		$("#telephoneInfo").html("不能为空");
	}else{
		$("#telephoneInfo").html("");
	}
	if(regionName!=""&&dutyUser!=""&&portal!=""&&telephone!=""){
		status = false;
	}
	if(!status){
		disable();
		if(editStatus){
			polygonEditor.close(); 
			$("#edit").attr('disabled',true);  
			$("#remove").attr('disabled',true);  
			$("#save").attr('disabled',true); 
			$("#cancel").attr('disabled',true);
			$("#add").attr('disabled',false); 
		}
		if(addStatus){
			addStatus = false;
			var companyName = $("#area").val();
			var businessName = $("#part").val();
			var dutyName = $("#town").val();
			$("#save").attr('disabled',true); 
			$("#cancel").attr('disabled',true);
			$("#add").attr('disabled',false);  
			var lnglat = ""+overlay.obj;
			$.post("./save",{
				"lnglat":lnglat,
				"companyName":companyName,
				"businessName":businessName,
				"dutyName":dutyName,
				"regionName":regionName,
				"dutyUser":dutyUser,
				"portal":portal,
				"telephone":telephone,
				"scenesLabel":scenesLabel,
				"businessLabel":businessLabel,
				"featureLabel":featureLabel
			},function(data){
				if(data.status==0){
					alert("保存成功");
					if(overlay!=null){
						map.remove(overlay.obj);
						overlay=null;
					}
					if(allRegion!=""){
						map.remove(allRegion);
						allRegion = [];
					}
					getRegion(regionName,true);
				}else{
					//alert(data.info);
				}
			},"json");
		}
	}
}

//获取微区域列表，更新微区域的下拉框
function getRegion(regionName,status){
	if(regionPolygons!=""){
		map.remove(regionPolygons);
		regionPolygons = "";
	}
	$("#region").empty();
	$("#region").append('<option value="">显示全部</option>');
	$("#regionName").val("");
	$("#dutyUser").val("");
	$("#portal").val("");
	$("#telephone").val("");
	$('input[name="scenesLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	$('input[name="businessLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	$('input[name="featureLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	var companyName = $("#area").val();
	var businessName = $("#part").val();
	var dutyName = $("#town").val();
	$.post("./listRegion",{
		"companyName":companyName,
		"businessName":businessName,
		"dutyName":dutyName
	},function(data){
		if(data.status==0){
			regionList = data.regionList;
			var town = $("#town").val();
			for(var i=0;i<regionList.length;i++){
				if(town==regionList[i].dutyName){
					$("#region").append('<option value="'+regionList[i].regionName+'">'+regionList[i].regionName+'</option>');
				}
			}
			if(regionName!=null){
				$("#region").val(regionName);
			}
			//如果status为true，则表示新增或者编辑网格时的调用，此时需要位置微区域
			if(status){
				drawRegion(regionName);
			}
			if($("#regionName").val()==""){
				drawAllRegion();
			}
		}else{
			//alert(data.info);
		}
	},"json");
}

//编辑按钮
function edit(){
	undisable();
	editStatus = true;
	addStatus = false;
	polygonEditor.open();
	$("#edit").attr('disabled',true);  
	$("#remove").attr('disabled',true);  
	$("#add").attr('disabled',true);  
	$("#save").attr('disabled',false); 
	$("#cancel").attr('disabled',false);
}

//删除按钮
function remove(){
	var region = $("#region").val();
	if(confirm("确定要删除该微区域吗")){
		$.post("./delete",{
			"regionId":regionId
		},function(data){
			if(data.status==0){
				alert("删除成功");
				changeTown();
			}
		},"json")
    }else{
    	return false;
    }
}

//分公司、业务网格
function draw(lnglats,status){
	if(polygons2!=null){
		map.remove(polygons2);
	}
	map.remove(polygons);
	//绘制多边形
	var path = [];
	for(var j=0;j<lnglats.length;j++){
		var lnglat = [];
		lnglat.push(lnglats[j].split(",")[0]);
		lnglat.push(lnglats[j].split(",")[1]);
		path.push(lnglat);
	}
	polygons = new AMap.Polygon({
        path: path,
        strokeColor: "red", 
        strokeWeight: 4,
        strokeOpacity: 1,
        fillOpacity: 0,
        zIndex: 45,
    })
	polygons.on('rightclick', rightClick);
	map.add(polygons);
	//绘制多边形
	if(!status){
		map.setFitView(polygons);//视口自适应
	}
}

//绘制汕头市网格
function drawBounds(){
	if(polygons2!=null){
		map.remove(polygons2);
	}
	if(polygons!=null){
		map.remove(polygons);
	}
	AMap.plugin([
	    'AMap.ToolBar',
	    'AMap.Scale',
	    'AMap.OverView',
	    'AMap.MapType',
	    'AMap.Geolocation',
	], function(){
	    // 在图面添加工具条控件，工具条控件集成了缩放、平移、定位等功能按钮在内的组合控件
	    //map.addControl(new AMap.ToolBar());
	    // 在图面添加比例尺控件，展示地图在当前层级和纬度下的比例尺
	    map.addControl(new AMap.Scale());
	});
	var district = null;
	//加载行政区划插件
	if(!district){
	    //实例化DistrictSearch
	    var opts = {
	        subdistrict: 0,   //获取边界不需要返回下级行政区
	        extensions: 'all',  //返回行政区边界坐标组等具体信息
	        level: 'district'  //查询行政级别为 市
	    };
	    district = new AMap.DistrictSearch(opts);
	}

	//行政区查询
	district.setLevel("city");
	district.search("汕头", function(status, result) {
	    polygons = [];
	    var bounds = result.districtList[0].boundaries;
	    if (bounds) {
	        for (var i = 0, l = bounds.length; i < l; i++) {
	            //生成行政区划polygon
	            var polygon = new AMap.Polygon({
	                strokeWeight: 4,
	                path: bounds[i],
	                fillOpacity: 0,
	                strokeColor: 'red'
	            });
	            polygons.push(polygon);
	            polygon.on('rightclick', rightClick);
	        }
	    }
	    map.add(polygons);
	    map.setFitView(polygons);//视口自适应
	});
}


//切换到谷歌卫星地图
$("#googleAddress").click(function(){
	$("#googleAddress").attr('disabled',true);
	$("#gaodeAddress").attr('disabled',false);
	satelliteMap();
})

//切换到高德地图
$("#gaodeAddress").click(function(){
	$("#googleAddress").attr('disabled',false);
	$("#gaodeAddress").attr('disabled',true);
	gaodeMap();
})

//切换到谷歌卫星地图具体实现
function satelliteMap(){
    var zoom = map.getZoom(); //获取当前地图级别
    var center = map.getCenter(); //获取当前地图中心位置
    if(map!=null){
    	map.destroy();
    }
	map = new AMap.Map('map', {
	    center: [center.lng,center.lat], //设置中心点
	    zoom: zoom, //设置缩放级别
	    layers:[googleLayer,roadNetLayer] //设置图层
	});
	mouseTool = new AMap.MouseTool(map); 
	overlay = null;
	mouseTool.on('draw',draw1);
	map.on('rightclick', rightClick);
	setAddressList();
	setCurrentGrid();
	if(allRegion != []){
		drawAllRegion();
	}
}

//切换到高德地图具体实现
function gaodeMap(){
	var zoom = map.getZoom(); //获取当前地图级别
    var center = map.getCenter(); //获取当前地图中心位置
    if(map!=null){
    	map.destroy();
    }
    map = new AMap.Map('map',{
    	center: [center.lng,center.lat], //设置中心点
	    zoom: zoom, //设置缩放级别
    });
	mouseTool = new AMap.MouseTool(map); 
	overlay = null;
	mouseTool.on('draw',draw1);
	map.on('rightclick', rightClick);
	setAddressList();
	setCurrentGrid();
	if(allRegion != []){
		drawAllRegion();
	}
}

//切换地图时重新绘制网格
function setCurrentGrid(){
	map.add(polygons);
	map.add(polygons2);
	if(gridStatus){
		drawGridList();
	}
	if(regionPolygons!=""){
		regionPolygons.on('rightclick', rightClick);
		map.add(regionPolygons);
		//构造折线编辑对象，并开启折线的编辑状态
		map.plugin(["AMap.PolyEditor"],function(){
			polygonEditor = new AMap.PolyEditor(map,regionPolygons);
			polygonEditor.on('end', function(event) {
				if(editStatus){
					var regionName = $("#regionName").val();
					var dutyUser = $("#dutyUser").val();
					var portal = $("#portal").val();
					var telephone = $("#telephone").val();
					var companyName = $("#area").val();
					var businessName = $("#part").val();
					var dutyName = $("#town").val();
					var scenesLabel = "";
					$('input[name="scenesLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
						scenesLabel = scenesLabel + "," + $(this).val();
					});
					var businessLabel = "";
					$('input[name="businessLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
						businessLabel = businessLabel + "," + $(this).val();
					});
					var featureLabel = "";
					$('input[name="featureLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
						featureLabel = featureLabel + "," + $(this).val();
					});
					editStatus = false;
					var lnglat = ""+event.target;
					$.post("./edit",{
						"regionId":regionId,
						"lnglat":lnglat,
						"companyName":companyName,
						"businessName":businessName,
						"dutyName":dutyName,
						"regionName":regionName,
						"dutyUser":dutyUser,
						"portal":portal,
						"telephone":telephone,
						"scenesLabel":scenesLabel,
						"businessLabel":businessLabel,
						"featureLabel":featureLabel
					},function(data){
						if(data.status==0){
							alert("编辑成功");
							getRegion(regionName,true);
						}
					},"json")
				}
			})
	    });
	}
}

//清空缓存数据
function empty(){
	$("#power").html("");
	$("#regionName").val("");
	$("#dutyUser").val("");
	$('input[name="scenesLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	$('input[name="businessLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	$('input[name="featureLabel"]:checked').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).removeAttr("checked");
	});
	$("#portal").val("");
	$("#telephone").val("");
	$("#regionNameInfo").html("");
	$("#dutyUserInfo").html("");
	$("#portalInfo").html("");
	$("#telephoneInfo").html("");
	$('#gridTable tr td').remove();
	$("#previous").attr('disabled',true);
	$("#next").attr('disabled',true);
	$("#gridButton").attr('disabled',true);
	$("#gridButton").html("显示微网格");
	gridStatus = true;
	if(regionPolygons!=""){
		map.remove(regionPolygons);
		regionPolygons = "";
	}
	if(gridElements!=""){
		map.remove(gridElements);
		gridElements = [];
	}
	$("#add").attr('disabled',true);
	$("#edit").attr('disabled',true);  
	$("#remove").attr('disabled',true);  
	$("#save").attr('disabled',true); 
	$("#cancel").attr('disabled',true);
	if(allRegion != []){
		map.remove(allRegion);
		allRegion = "";
	}
}

//禁用
function disable(){
	$("#regionName").attr('disabled',true);
	$("#dutyUser").attr('disabled',true);  
	$("#portal").attr('disabled',true);  
	$("#telephone").attr('disabled',true); 
	$('input[name="scenesLabel"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).attr('disabled',true); 
	});
	$('input[name="businessLabel"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).attr('disabled',true); 
	});
	$('input[name="featureLabel"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).attr('disabled',true); 
	});
}

//启用
function undisable(){
	$("#regionName").attr('disabled',false);
	$("#dutyUser").attr('disabled',false);  
	$("#portal").attr('disabled',false);  
	$("#telephone").attr('disabled',false); 
	$('input[name="scenesLabel"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).attr('disabled',false); 
	});
	$('input[name="businessLabel"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).attr('disabled',false); 
	});
	$('input[name="featureLabel"]').each(function(){//遍历每一个名字为interest的复选框，其中选中的执行函数
	    $(this).attr('disabled',false); 
	});
}
