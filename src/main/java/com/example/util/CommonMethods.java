package com.example.util;

import java.awt.geom.Point2D;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import com.alibaba.fastjson.JSONObject;
import com.example.dto.GaodeReverseGeoCodeEntity;
import com.example.dto.GeocoderDto;
import com.example.vo.AddressVo;
import com.example.vo.PoiVo;

/**
 * 多边形计算服务
 * @author chinamobile
 *
 */
public class CommonMethods {
	
	
	// 默认除法运算精度
    private static final Integer DEF_DIV_SCALE = 6;
	
	/**
	 * 判断是否是数字
	 * @param str
	 * @return
	 */
	public static boolean isNumber(String str){
		String reg = "^[0-9]+(.[0-9]+)?$";
		return str.matches(reg);
	}
	
	/**
	 * 判断字符串是否以数字开头
	 * @param str
	 * @return
	 */
	public static boolean isStartWithNumber(String str) {
		 Pattern pattern = Pattern.compile("[0-9]*");
		 Matcher isNum = pattern.matcher(str.charAt(0)+"");
		 if (!isNum.matches()) {
		       return false;
		 }
		 return true;
	}
	
	/*2018年3月已知
	中国电信号段
    	133,149,153,173,177,180,181,189,199
	中国联通号段
    	130,131,132,145,155,156,166,175,176,185,186
	中国移动号段
    	134(0-8),135,136,137,138,139,147,150,151,152,157,158,159,178,182,183,184,187,188,198
	其他号段
    	14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
       虚拟运营商
		电信：1700,1701,1702
		移动：1703,1705,1706
		联通：1704,1707,1708,1709,171
	卫星通信：148(移动) 1349
	*/
	public static boolean isMobile(String str) {
	    Pattern p = null;
	    Matcher m = null;
	    boolean b = false;
	    String s2="^[1](([3][0-9])|([4][5,7,9])|([5][4,6,9])|([6][6])|([7][3,5,6,7,8])|([8][0-9])|([9][8,9]))[0-9]{8}$";// 验证手机号
	    if(StringUtils.isNotBlank(str)){
	        p = Pattern.compile(s2);
	        m = p.matcher(str);
	        b = m.matches();
	    }
	    return b;
	}
	
	 /**
     * 提供精确的加法运算。
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static Double add(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.add(b2).doubleValue();
    }
 
    /**
     * 提供精确的减法运算。
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.subtract(b2).doubleValue();
    }
 
    /**
     * 提供精确的乘法运算。
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static Double mul(Double value1, Double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }
 
    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时， 精确到小数点以后10位，以后的数字四舍五入。
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @return 两个参数的商
     */
    public static Double divide(Double dividend, Double divisor) {
        return divide(dividend, divisor, DEF_DIV_SCALE);
    }
 
    /**
     * 提供（相对）精确的除法运算。 当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
     *
     * @param dividend 被除数
     * @param divisor  除数
     * @param scale    表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    public static Double divide(Double dividend, Double divisor, Integer scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(dividend));
        BigDecimal b2 = new BigDecimal(Double.toString(divisor));
        return b1.divide(b2, scale,RoundingMode.HALF_UP).doubleValue();
    }
 
    /**
     * 提供指定数值的（精确）小数位四舍五入处理。
     *
     * @param value 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double value,int scale){
        if(scale<0){
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b = new BigDecimal(Double.toString(value));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one,scale, RoundingMode.HALF_UP).doubleValue();
    } 
  
	
    /**
     * 判断二维平面上，判断多边形polygon1是否与多边形polygon2相交，是返回true，不是和异常返回false
     * @param polygon1 多边形的各个点，必须首尾点相同，点的两个坐标逗号隔开，点与点之间分号隔开
     * @param polygon2 多边形的各个点，必须首尾点相同，点的两个坐标逗号隔开，点与点之间分号隔开
     * @return
     */
    public static boolean isTwoPolygonsHasContainedRelation(String polygon1, String polygon2) {
    	boolean flag=false;
    	WKTReader reader = null;
		Geometry geometry1=null;
		Geometry geometry2=null;
		String[] polyPoints=null;
    	String[] polyPoint=null;
    	String tmpPolygon=null;
		String wktPoly1=null;
    	String wktPoly2=null;
		List<String> allPolygons=null;
		int lastPos=0;
		
		if(polygon1==null||polygon2==null||StringUtils.equals(polygon1.trim(), "")==true||StringUtils.equals(polygon2.trim(), "")==true)return flag;
		
		//多边形合法性的判断，首尾是否相同，是否点都是数字
		allPolygons=new ArrayList<String>();
		allPolygons.add(polygon1);
		allPolygons.add(polygon2);
    	for(int i=0;i<allPolygons.size();i++) {
    		tmpPolygon=allPolygons.get(i);
    		if(tmpPolygon==null||StringUtils.contains(tmpPolygon, ",")==false||StringUtils.contains(tmpPolygon, ";")==false)return flag;
    		
    		polyPoints=tmpPolygon.split(";");
    		lastPos=polyPoints.length-1;
    		if(lastPos<3||StringUtils.equals(polyPoints[0],polyPoints[lastPos])==false)return flag;//首尾点必须相同
    		
    		for(int j=0;j<polyPoints.length;j++) {
        		polyPoint=polyPoints[j].split(",");
        		if(polyPoint==null||polyPoint.length!=2||isNumber(polyPoint[0])==false||isNumber(polyPoint[1])==false)return flag;
        	}
    		
    		reader=new WKTReader(JTSFactoryFinder.getGeometryFactory());
    		
    		wktPoly1=polygon1.replaceAll(",", " ");
    		wktPoly1=wktPoly1.replaceAll(";", ",");
    		wktPoly1="POLYGON (("+wktPoly1+"))";
    		
    		wktPoly2=polygon2.replaceAll(",", " ");
    		wktPoly2=wktPoly2.replaceAll(";", ",");
    		wktPoly2="POLYGON (("+wktPoly2+"))";
    		
    		try {
    			geometry1=reader.read(wktPoly1);
    			geometry2=reader.read(wktPoly2);
    			flag=geometry1.contains(geometry2);//测试准确，可以使用
    		} catch (ParseException e) {
    			e.printStackTrace();
    			flag=false;
    		}    			
    	}
    	return flag;
    }
    
    /**
     * 判断二维平面上，判断多边形polygon1是否包含多边形polygon2，是返回true，不是和异常返回false
     * @param polygon1 多边形的各个点，必须首尾点相同，点的两个坐标逗号隔开，点与点之间分号隔开
     * @param polygon2 多边形的各个点，必须首尾点相同，点的两个坐标逗号隔开，点与点之间分号隔开
     * @return
     */
    public static boolean isTwoPolygonsHasInterSectionRelation(String polygon1, String polygon2) {
    	boolean flag=false;
    	WKTReader reader = null;
		Geometry geometry1=null;
		Geometry geometry2=null;
		String[] polyPoints=null;
    	String[] polyPoint=null;
    	String tmpPolygon=null;
		String wktPoly1=null;
    	String wktPoly2=null;
		List<String> allPolygons=null;
		int lastPos=0;
		
		if(polygon1==null||polygon2==null||StringUtils.equals(polygon1.trim(), "")==true||StringUtils.equals(polygon2.trim(), "")==true)return flag;
		
		//多边形合法性的判断，首尾是否相同，是否点都是数字
		allPolygons=new ArrayList<String>();
		allPolygons.add(polygon1);
		allPolygons.add(polygon2);
    	for(int i=0;i<allPolygons.size();i++) {
    		tmpPolygon=allPolygons.get(i);
    		if(tmpPolygon==null||StringUtils.contains(tmpPolygon, ",")==false||StringUtils.contains(tmpPolygon, ";")==false)return flag;
    		
    		polyPoints=tmpPolygon.split(";");
    		lastPos=polyPoints.length-1;
    		if(lastPos<3||StringUtils.equals(polyPoints[0],polyPoints[lastPos])==false)return flag;//首尾点必须相同
    		
    		for(int j=0;j<polyPoints.length;j++) {
        		polyPoint=polyPoints[j].split(",");
        		if(polyPoint==null||polyPoint.length!=2||isNumber(polyPoint[0])==false||isNumber(polyPoint[1])==false)return flag;
        	}
    		
    		reader=new WKTReader(JTSFactoryFinder.getGeometryFactory());
    		
    		wktPoly1=polygon1.replaceAll(",", " ");
    		wktPoly1=wktPoly1.replaceAll(";", ",");
    		wktPoly1="POLYGON (("+wktPoly1+"))";
    		
    		wktPoly2=polygon2.replaceAll(",", " ");
    		wktPoly2=wktPoly2.replaceAll(";", ",");
    		wktPoly2="POLYGON (("+wktPoly2+"))";
    		
    		try {
    			geometry1=reader.read(wktPoly1);
    			geometry2=reader.read(wktPoly2);
    			flag=geometry1.intersects(geometry2);//测试准确，可以使用
    		} catch (ParseException e) {
    			e.printStackTrace();
    			flag=false;
    		}    			
    	}
    	return flag;
    }
    
    /**
     * 创建方向向量为中线的120度扇形区域
     * @param startGps 圆心
     * @param endGps 方向向量的结束点
     * @param radius 米的距离
     * @return
     */
    public static String createSector(String startGps, String endGps, double radius) {
    	//扇形按照PI/18.0作为间隔弧度，进行模拟多边形点的生成
    	String polygon=null;
    	String[] startPts=null;
    	String[] endPts=null;
    	double theta=0.0;
    	double[] stPts=null;
    	double[] edPts=null;
    	double[] midPts=null;
    	double[] tmpPts=null;
    	List<double[]> antiClockWise=null;
    	List<double[]> clockWise=null;
    	
    	if(radius<=0||startGps==null||endGps==null||StringUtils.equals(startGps.trim(), "")==true||StringUtils.equals(endGps.trim(), "")==true) {
    		return polygon;
    	}
    	
    	if(StringUtils.contains(startGps, ",")==false||StringUtils.contains(endGps, ",")==false) {
    		return polygon;
    	}
    	
    	if(StringUtils.contains(endGps, ",")==false||StringUtils.contains(endGps, ",")==false) {
    		return polygon;
    	}
    	
    	startPts=startGps.split(",");
    	endPts=endGps.split(",");
    	
    	if(startPts==null||endPts==null||startPts.length!=2||endPts.length!=2) {
    		return polygon;
    	}
    	
    	if(isNumber(startPts[0])==false||isNumber(startPts[1])==false||isNumber(endPts[0])==false||isNumber(endPts[1])==false) {
    		return polygon;
    	}
    	
    	//校验半径中线对应的向量重点
    	stPts=new double[2];
    	edPts=new double[2];
    	midPts=new double[2];
    	
    	stPts[0]=Double.valueOf(startPts[0]).doubleValue();
    	stPts[1]=Double.valueOf(startPts[1]).doubleValue();
    	
    	edPts[0]=Double.valueOf(endPts[0]).doubleValue();
    	edPts[1]=Double.valueOf(endPts[1]).doubleValue();

    	//获取原有向量的弧度，注意不是角度，而是弧度
    	theta=Math.atan2(edPts[1]-stPts[1],edPts[0]-stPts[0]);
    	//按照0.0001代表10米计算，必须换算成经纬度点之间的距离差距，更新向量终点
    	edPts[0]=radius*0.00001*Math.cos(theta)+stPts[0];
    	edPts[1]=radius*0.00001*Math.sin(theta)+stPts[1];
    	
    	//记录中线向量终点
    	midPts[0]=edPts[0];
    	midPts[1]=edPts[1];

    	polygon=stPts[0]+","+stPts[1]+";";
    	
    	//逆时针新建圆弧点
    	theta=Math.PI/18.0; //固定值
    	antiClockWise=new ArrayList<double[]>();
    	for(int i=1;i<=6;i++) {
    		tmpPts=new double[2];//计算当前向量逆时针旋转后的新的圆弧终点
    		tmpPts[0]=((edPts[0]-stPts[0])*Math.cos(theta)-(edPts[1]-stPts[1])*Math.sin(theta))+stPts[0];
    		tmpPts[1]=((edPts[0]-stPts[0])*Math.sin(theta)+(edPts[1]-stPts[1])*Math.cos(theta))+stPts[1];
    		antiClockWise.add(tmpPts);
    		edPts[0]=tmpPts[0];
    		edPts[1]=tmpPts[1];
    	}
    	
    	edPts[0]=midPts[0];
    	edPts[1]=midPts[1];
    	//顺时针新建圆弧点
    	theta=(Math.PI/18.0)*35.0; //固定值
    	clockWise=new ArrayList<double[]>();
    	for(int i=1;i<=6;i++) {
    		tmpPts=new double[2];//计算当前向量逆时针旋转后的新的圆弧终点
    		tmpPts[0]=((edPts[0]-stPts[0])*Math.cos(theta)-(edPts[1]-stPts[1])*Math.sin(theta))+stPts[0];
    		tmpPts[1]=((edPts[0]-stPts[0])*Math.sin(theta)+(edPts[1]-stPts[1])*Math.cos(theta))+stPts[1];
    		clockWise.add(tmpPts);
    		edPts[0]=tmpPts[0];
    		edPts[1]=tmpPts[1];
    	}
    	
    	//先获取顺时针的点
    	for(int i=5;i>0;i--) {
    		tmpPts=clockWise.get(i);
    		polygon+=tmpPts[0]+","+tmpPts[1]+";";
    	}
    	
    	//取扇形中点
    	polygon+=midPts[0]+","+midPts[1]+";";
    	
    	//取逆时针的点
    	for(int i=0;i<6;i++) {
    		tmpPts=antiClockWise.get(i);
    		polygon+=tmpPts[0]+","+tmpPts[1]+";";
    	}
    	polygon+=stPts[0]+","+stPts[1];
    	return polygon;
    }
    
    /**
     * 判断二维平面上，点是否包含在多边形中
     * @param point x,y两个数值，逗号隔开
     * @param polygon	多边形的各个点，必须首尾点相同，点的两个坐标逗号隔开，点与点之间分号隔开
     * @return
     */
    public static boolean isPointInPolygon(String point, String polygon) {
    	boolean flag=false;
    	String[] xyPoint=null;
    	String[] polyPoints=null;
    	String[] polyPoint=null;
    	String wktPoint=null;
    	String wktPoly=null;
    	int lastPos=0;
    	WKTReader reader = null;
		Geometry geometry=null;
		Geometry geopoint=null;
    	
    	if(point==null||polygon==null
    	||StringUtils.contains(point, ",")==false
    	||StringUtils.contains(polygon, ",")==false||StringUtils.contains(polygon, ";")==false)return flag;
    	
    	//点的判断
    	xyPoint=point.split(",");
    	if(xyPoint==null||xyPoint.length!=2||isNumber(xyPoint[0])==false||isNumber(xyPoint[1])==false)return flag;
    	wktPoint="POINT ("+xyPoint[0]+" "+xyPoint[1]+")";
    	
    	//多边形合法性的判断，首尾是否相同，是否点都是数字
    	polyPoints=polygon.split(";");
    	if(polyPoints==null)return flag;
    	
    	lastPos=polyPoints.length-1;
    	if(lastPos<3||StringUtils.equals(polyPoints[0],polyPoints[lastPos])==false)return flag;
    	for(int i=0;i<polyPoints.length;i++) {
    		polyPoint=polyPoints[i].split(",");
    		if(polyPoint==null||polyPoint.length!=2||isNumber(polyPoint[0])==false||isNumber(polyPoint[1])==false)return flag;
    	}
    	
    	wktPoly=polygon.replaceAll(",", " ");
    	wktPoly=wktPoly.replaceAll(";", ",");
    	wktPoly="POLYGON (("+wktPoly+"))";
    	
    	reader=new WKTReader(JTSFactoryFinder.getGeometryFactory());
    	try {
			geometry=reader.read(wktPoly);
			geopoint=reader.read(wktPoint);
			flag=geometry.contains(geopoint);
		} catch (ParseException e) {
			e.printStackTrace();
			flag=false;
		}    	
    	return flag;
    }
    
    /**
	 * 主入口方法：对二维平面的多边形进行面积计算
	 * @param polygonPoints 多边形，首尾点相同，并且点之间;隔开，点的坐标之间,隔开
	 * @return
	 */
	public static double computePolygonArea(String polygonPoints) {
		String[] points=null;
		String[] point=null;
		int lastPos=0;
		List<Point2D.Double> pointsList=new ArrayList<Point2D.Double>();
		Point2D.Double point2d=null;
		double area=0.0;
		
		if(polygonPoints==null||StringUtils.contains(polygonPoints, ",")==false||StringUtils.contains(polygonPoints, ";")==false)return 0.0;
		
		points=polygonPoints.split(";");
		lastPos=points.length-1;
		if(lastPos<3||StringUtils.equals(points[0],points[lastPos])==false)return 0.0;//首尾点必须相同，并且多边形必须满足至少3条边
		
		for(int i=0;i<points.length-1;i++) {//去掉尾部的相同点
			point=points[i].split(",");
    		if(point==null||point.length!=2||isNumber(point[0])==false||isNumber(point[1])==false)return 0.0;
    		point2d=new Point2D.Double(Double.valueOf(point[0]).doubleValue(), Double.valueOf(point[1]).doubleValue());
    		pointsList.add(point2d);
    	}
		area=computePolygonArea(pointsList);
		
		return area;
	}
	
	/**
	 * 计算任意多边形的面积
	 * @param pointsList 任意多边形的点
	 * @return
	 */
	public static double computePolygonArea(List<Point2D.Double> pointsList) {
		TreeSet<Point2D.Double> polygonPoints=null;
		double area=0.0;
		int num=0;
		List<Point2D.Double> pList=null;
		try {
			polygonPoints=GrahamScan.getSortedPointTreeSet(pointsList);//对数据进行逆时针排序
			if(polygonPoints==null||polygonPoints.size()<3) return 0.0;
			
			pList=new ArrayList<Point2D.Double>(polygonPoints);
			num=pList.size();
			area=pList.get(0).y*(pList.get(num-1).x-pList.get(1).x);
			for(int i=1;i<num;++i) {
				area += pList.get(i).y * (pList.get(i-1).x - pList.get((i+1)%num).x);
			}
			area=Math.abs(area/2.0);
		}catch(Exception ex){
			area=0.0;
		}
		return area;
	}
	
	/**
	 * 获取多边形经纬度
	 * @param polygon[0]为高德
	 * @param polygon[1]为GPS
	 * @param polygon[2]为百度
	 * @return
	 */
	public static List<String> getCenterPoint(String polygon){
		List<String> list = new ArrayList<String>();
		String[] array = polygon.split(";");
		double lng = 0.0;
		double lat = 0.0;
		for(int i=0;i<array.length;i++){
			lng = lng + Double.valueOf(array[i].split(",")[0]);
			lat = lat + Double.valueOf(array[i].split(",")[1]);
		}
		lng = lng/array.length;
		lat = lat/array.length;
		String gridCtGaode = lng + "," + lat;
		double[] gps = BDToGPS.gcj2WGSExactly(lat,lng);
		String gridCtGps = gps[1] + "," +gps[0];
		double[] baidu = BDToGPS.gcj2BD09(lat,lng);
		String gridCtBaidu = baidu[1] + "," + baidu[0];
		list.add(gridCtGaode);
		list.add(gridCtGps);
		list.add(gridCtBaidu);
		return list;
	}
	
	public static GaodeReverseGeoCodeEntity getGaoDeInfo(String lnglat){
		String url = "https://restapi.amap.com/v3/geocode/regeo?output=json&key=7954677eb18790b3e5cd00bb62d6747c&location="+lnglat.split(",")[0]+","+lnglat.split(",")[1];
		String json = HttpUtil.sendGet(url);
		GaodeReverseGeoCodeEntity codeEntity = JSONObject.parseObject(json,GaodeReverseGeoCodeEntity.class);
		return codeEntity;
	}
	
	public static GeocoderDto getBaiduInfo(String lnglat){
		String url = "http://api.map.baidu.com/geocoder/v2/?output=json&extensions_town=true&ak=ZEHNRDwU96vA3QOn8epDTG4qB8S7pRGa&location="+lnglat.split(",")[1]+","+lnglat.split(",")[0];
		String json = HttpUtil.sendGet(url);
		GeocoderDto geocoderDto = JSONObject.parseObject(json, GeocoderDto.class);
		return geocoderDto;
	}
	
	public static List<PoiVo> getPois(String name) {
		try {
			name=URLEncoder.encode(name,"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String url = "https://restapi.amap.com/v3/place/text?keywords="+name+"&city=shantou&output=json&offset=10&page=1&key=f08437e18b0f4e10efd3f668636774e8";
		String json = HttpUtil.sendGet(url);
		AddressVo addressVo = JSONObject.parseObject(json, AddressVo.class);
		if (addressVo.getStatus().equals("1")) {
			return addressVo.getPois();
		}else{
			return null;
		}
	}
    
    /**
     * 代码测试
     * 
     */
    public static void main(String[] args) {
    	try {
			List<String> bsList = FileUtil.readTxtFile("C:/Users/Administrator/Desktop/网络部/基站详细信息.txt");
			List<String> areaList = FileUtil.readTxtFile("C:/Users/Administrator/Desktop/网络部/园区信息.txt");
			List<String> list = new ArrayList<String>();
			String content = "";
			for(int i=1;i<areaList.size();i++){
				System.out.println(i);
				String[] areaArray = areaList.get(i).split("\\|");
				String areaBaidu = areaArray[5] + "," + areaArray[6] + ";" + areaArray[7] + "," + areaArray[8] + ";" +
						areaArray[9] + "," + areaArray[10] + ";" + areaArray[11] + "," + areaArray[12] + ";" + areaArray[5] + "," + areaArray[6];
				for(int j=1;j<bsList.size();j++){
					content = i+"|"+areaArray[0]+"|"+areaArray[1]+"|"+areaArray[2]+"|"+areaArray[3]+"|"+areaArray[4]+"|";
					String[] bsArray = bsList.get(j).split("\\|");
					String baidu = bsArray[8];
					if (isTwoPolygonsHasInterSectionRelation(baidu, areaBaidu)) {
						content = content + bsArray[0] + "|" + bsArray[1] + "|" + bsArray[2] + "|" + bsArray[3] + "|" + bsArray[4] + "|" + bsArray[5] + "|" + bsArray[6];
						list.add(content);
					}
				}
			}
			FileUtil.writeTxtFile(list, "C:/Users/Administrator/Desktop/网络部/园区基站明细数据.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//    	String gaode = "116.703226,23.384356;116.701746,23.382829;116.70446,23.380515;116.706466,23.38344;116.703226,23.384356";
//    	String gps = "";
//    	String[] array = gaode.split(";");
//    	for(int i=0;i<array.length;i++){
//    		double[] lnglat = BDToGPS.gcj2WGSExactly(Double.valueOf(array[i].split(",")[1]), Double.valueOf(array[i].split(",")[0]));
//    		System.out.println(lnglat[1]+","+lnglat[0]);
//    		if (i==array.length-1) {
//    			gps = gps + lnglat[1]+","+lnglat[0];
//			}else{
//				gps = gps + lnglat[1]+","+lnglat[0]+";";
//			}
//    	}
//    	System.out.println(gaode);
//    	System.out.println(gps);
//    	System.out.println(computePolygonArea(gps));
//    	System.out.println(computePolygonArea(gaode));
//    	boolean flag=false;	
//    	String point="116.75119237204896,23.46497244164877";
//    	String polygon="116.217173,23.394834;116.651133,23.754814;117.269114,23.540959;117.016428,22.995954;116.233652,22.940319;116.217173,23.394834";
//    	flag=isPointInPolygon(point, polygon);
//    	for(int i=0;i<50000;i++){
//    		String polygon1=CommonMethods.createSector("116.651133,23.754814", "117.016428,22.995954", 500);
//        	String polygon2="116.217173,23.394834;116.651133,23.754814;117.269114,23.540959;117.016428,22.995954;116.233652,22.940319;116.217173,23.394834";
//        	Boolean status = isTwoPolygonsHasInterSectionRelation(polygon1, polygon2);
//        	System.out.println(i+":"+status);
//    	}
//    	
//		double y1=116.58175999941611-116.57976;
//		double y2=116.58093417863-116.57976;
//		
//		double x1=23.424548471754612-23.42455;
//		double x2=23.4245491027832-23.42455;
//
//		System.out.println(y1/x1);
//		System.out.println(y2/x2);
    	
//    	String text="1;2;3";
//    	String[] tArr=null;
//    	tArr=text.split(";");
//		System.out.println(tArr.length);
//		
//		text="1";
//		tArr=text.split(";");
//		System.out.println(tArr.length);
//		
//		text="a;;;;;a";//字符串会自动省略后续的分号信息
//		tArr=text.split(";");
//		System.out.println(tArr.length);
    }
}
