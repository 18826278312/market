var powerList = [];
var companyList = [];
var businessList = [];
var dutyList = [];
var index = -1;

//获取权限数据
$.post("./getPower",{
},function(data){
	if(data.status==0){
		powerList = data.powerList;
		companyList = data.companyList;
		businessList = data.businessList;
		dutyList = data.dutyList;
		setTable();
	}else{
		alert(data.info);
	}
},"json");

//设置权限控制列表
function setTable(){
	$('#table tr td').remove();
	for(var i=0;i<powerList.length;i++){
		var company = "";
		var business = "";
		var duty = "";
		for(var j=0;j<powerList[i].companyValue.length;j++){
			company = company + "<span style='margin-left:5px;' class='label label-success'>" + powerList[i].companyValue[j] + "</span>";
		}
		for(var j=0;j<powerList[i].businessValue.length;j++){
			business = business + "<span style='margin-left:5px;' class='label label-success'>" + powerList[i].businessValue[j] + "</span>";
		}
		for(var j=0;j<powerList[i].dutyValue.length;j++){
			duty = duty + "<span style='margin-left:5px;' class='label label-success'>" + powerList[i].dutyValue[j] + "</span>";
		}
		var vTr= "<tr>" +
					"<td>"+ powerList[i].telephone +"</td>" +
					"<td>"+ powerList[i].userName +"</td>" +
					"<td>"+ powerList[i].company +"</td>" +
					"<td>"+ powerList[i].position +"</td>" +
					"<td>"+ company +"</td>" +
					"<td>"+ business +"</td>" +
					"<td>"+ duty +"</td>" +
					"<td>" +
						"<input type='button' value='修改权限'  onclick='updatePower("+i+")'/>" +
						"<input type='button' value='删除用户'  onclick='deletePower("+i+")'/>" +
					"</td>" +
				"</tr>";
		$("#table").append(vTr);
	}
}

//删除用户
function deletePower(i){
	if(window.confirm('你确定删除该吗？')){
		$.post("./deletePower",{
			"telephone":powerList[i].telephone
		},function(data){
			if(data.status==0){
				alert("删除成功");
				empty();
				powerList = data.powerList;
				setTable();
			}else{
				alert(data.info);
			}
		},"json");
        return true;
     }else{
        return false;
    }
}

//点击修改权限按钮
function updatePower(i){
	index = i;
	$(".pop").css("display","block");
	$(".pop1").css("display","block");
	$(".point1").css("display","block");
	setLabel(i);
}

//设置标签
function setLabel(i){
	$("#company").html("");
	$("#business").html("");
	$("#duty").html("");
	var company = "";
	var business = "";
	var duty = "";
	for(var j=0;j<powerList[i].companyValue.length;j++){
		company = company + "<span onclick='removeCompany("+i+","+j+")' style='margin-left:5px;' class='label label-success'>" + powerList[i].companyValue[j] + "</span>";
	}
	for(var j=0;j<powerList[i].businessValue.length;j++){
		business = business + "<span onclick='removeBusiness("+i+","+j+")' style='margin-left:5px;' class='label label-success'>" + powerList[i].businessValue[j] + "</span>";
	}
	for(var j=0;j<powerList[i].dutyValue.length;j++){
		duty = duty + "<span onclick='removeDuty("+i+","+j+")' style='margin-left:5px;' class='label label-success'>" + powerList[i].dutyValue[j] + "</span>";
	}
	$("#company").html(company);
	$("#business").html(business);
	$("#duty").html(duty);
}

function removeCompany(i,j){
	powerList[i].companyValue.splice(j,1);
	setLabel(i);
}

function removeBusiness(i,j){
	powerList[i].businessValue.splice(j,1);
	setLabel(i);
}

function removeDuty(i,j){
	powerList[i].dutyValue.splice(j,1);
	setLabel(i);
}

$("#close").click(function(){
	empty();
})

$("#save").click(function(){
	$.post("./updatePower",{
		"powerString":JSON.stringify(powerList[index])
	},function(data){
		if(data.status==0){
			empty();
			powerList = data.powerList;
			setTable();
		}else{
			alert(data.info);
		}
	},"json");
})

function empty(){
	$(".pop").css("display","none");
	$(".pop1").css("display","none");
	$(".point1").css("display","none");
	$("#area1").val("");
	$("#area2").val("");
	$("#area3").val("");
	$("#part2").empty();
	$("#part2").append('<option value="">-业务网格-</option>');
	$("#part3").empty();
	$("#part3").append('<option value="">-业务网格-</option>');
	$("#town3").empty();
	$("#town3").append('<option value="">-责任田-</option>');
}

//选择分公司1
$("#area1").change(function(){
	var area1 = $("#area1").val();
	if(area1!=""&&powerList[index].companyValue.indexOf(area1)==-1){
		powerList[index].companyValue.push(area1);
		setLabel(index);
	}
})

//选择分公司2
$("#area2").change(function(){
	$("#part2").empty();
	$("#part2").append('<option value="">-业务网格-</option>');
	var area2 = $("#area2").val();
	if(area2!=""){
		for(var i=0;i<businessList.length;i++){
			if(area2==businessList[i].companyName){
				$("#part2").append('<option value="'+businessList[i].businessName+'">'+businessList[i].businessName+'</option>');
			}
		}
	}
})

//选择业务网格2
$("#part2").change(function(){
	var part2 = $("#part2").val();
	if(part2!=""&&powerList[index].businessValue.indexOf(part2)==-1){
		powerList[index].businessValue.push(part2);
		setLabel(index);
	}
})


//选择分公3
$("#area3").change(function(){
	$("#part3").empty();
	$("#part3").append('<option value="">-业务网格-</option>');
	var area3 = $("#area3").val();
	if(area3!=""){
		for(var i=0;i<businessList.length;i++){
			if(area3==businessList[i].companyName){
				$("#part3").append('<option value="'+businessList[i].businessName+'">'+businessList[i].businessName+'</option>');
			}
		}
	}
})

//选择业务网格3
$("#part3").change(function(){
	$("#town3").empty();
	$("#town3").append('<option value="">-责任田-</option>');
	var part3 = $("#part3").val();
	if(part3!=""){
		for(var i=0;i<dutyList.length;i++){
			if(part3==dutyList[i].businessName){
				$("#town3").append('<option value="'+dutyList[i].dutyName+'">'+dutyList[i].dutyName+'</option>');
			}
		}
	}
})

//选择业务网格3
$("#town3").change(function(){
	var town3 = $("#town3").val();
	if(town3!=""&&powerList[index].dutyValue.indexOf(town3)==-1){
		powerList[index].dutyValue.push(town3);
		setLabel(index);
	}
})

$("#addUser").click(function(){
	$(".pop").css("display","block");
	$(".pop2").css("display","block");
	$(".point2").css("display","block");
})

$("#closeUser").click(function(){
	$(".pop").css("display","none");
	$(".pop2").css("display","none");
	$(".point2").css("display","none");
})

$("#saveUser").click(function(){
	var telephone = $("#telephone").val();
	var userName = $("#userName").val();
	var company = $("#companySelect").val();
	var position = $("#position").val();
	if(telephone && telephone.length == 11){
		$.post("./savePower",{
			"telephone":telephone,
			"userName":userName,
			"company":company,
			"position":position
		},function(data){
			if(data.status==0){
				alert("保存成功");
				$(".pop").css("display","none");
				$(".pop2").css("display","none");
				$(".point2").css("display","none");
				powerList = data.powerList;
				setTable();
			}else{
				alert(data.info);
			}
		},"json");
	}else{
		alert("请输入正确的手机号");
	}
})
