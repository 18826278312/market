package com.example.util;

/**
 * 配置公共变量
 * @author chinamobile
 */
public class ResourceConfig {
	//统一训练分词的前缀信息
	private final static String DEFAULT_FILE_PREFIX="D:\\NetAddress\\";	//E:\\NetAddress\\

	//语义距离计算需要使用的训练模型数据，来自地址库的分词结果信息
	public final static String DEFAULT_SEMANTIC_PREFIX=DEFAULT_FILE_PREFIX+"Semantic\\";
	public final static String DEFAULT_SEMANTIC_TRAIN_FILE=DEFAULT_SEMANTIC_PREFIX+"SemanticTrain.txt";
	public final static String DEFAULT_SEMANTIC_MODEL_FILE=DEFAULT_SEMANTIC_PREFIX+"SemanticW2V.txt";

	//分词库信息
	public final static String DEFAULT_DATA_CITY_FILE=DEFAULT_FILE_PREFIX+"NetAddressLevels\\1.city.txt"	;				//1级 地市
	public final static String DEFAULT_DATA_AREA_FILE=DEFAULT_FILE_PREFIX+"NetAddressLevels\\2.area.txt"	;				//2级 行政区域
	public final static String DEFAULT_DATA_TOWN_FILE=DEFAULT_FILE_PREFIX+"NetAddressLevels\\3.town.txt"	;			//3级 镇区，街道
	public final static String DEFAULT_DATA_ROAD_FILE=DEFAULT_FILE_PREFIX+"NetAddressLevels\\4.road.txt"	;				//4级 道路
	public final static String DEFAULT_DATA_BUILD_FILE=DEFAULT_FILE_PREFIX+"NetAddressLevels\\5.building.txt"	;		//5级 建筑群或建筑物
	public final static String DEFAULT_DATA_SIXTH_FILE=DEFAULT_FILE_PREFIX+"NetAddressLevels\\6.sixth.txt"	;			//6级 6级分割
	public final static String DEFAULT_DATA_SEVENTH_FILE=DEFAULT_FILE_PREFIX+"NetAddressLevels\\7.seventh.txt"	;	//7级 7级结尾
	public final static String DEFAULT_DATA_STREETTOWN_FILE=DEFAULT_FILE_PREFIX+"NetAddressLevels\\0.townStreets.txt";	//区县对应的街道镇区信息
	
	//代理配置，移动内部网络需要使用
	public final static String PROXY_HOST="10.244.155.137";
	public final static int PROXY_PORT=8081;
	
	//配置相关地图调用接口的参数，ak等信息
	//百度地图接口信息
	public final static String DEFAULT_BAIDU_AK="ZEHNRDwU96vA3QOn8epDTG4qB8S7pRGa";  			//ijfwv9AuV1fGln7IFVoBCarOs8GE0pxb
	public final static String DEFAULT_BAIDU_SEARCH_URL="http://api.map.baidu.com/place/v2/search?parameters";
	public final static int DEFAULT_BAIDU_PAGE_SIZE=1; 															//每页返回1条信息，默认地图的第一条记录就是最接近的地址
	public final static String DEFAULT_BAIDU_GEOCODE_URL="http://api.map.baidu.com/geocoder/v2/?parameters";
	public final static String DEFAULT_BAIDU_GEOCONV_URL="http://api.map.baidu.com/geoconv/v1/?parameters";
	public final static String DEFAULT_BAIDU_IP_URL="http://api.map.baidu.com/location/ip?parameters";
	public final static String DEFAULT_BAIDU_TRANSLATE_URL="http://api.map.baidu.com/geoconv/v1/?parameters";  //坐标转换接口
	
	//百度地图接口调用每天次数限制
	public final static long BAIDU_POI_SEARCH_TIMES=30000;
	public final static long BAIDU_GEO_REV_GEO_TIMES=300000;
	public final static long BAIDU_COOR_TRANS_TIMES=300000;
	
	//高德地图接口信息
	public final static String DEFAULT_GAODE_AK="07785350f2c6594e9fca433277105090";
	public final static String DEFAULT_GAODE_SEARCH_URL="https://restapi.amap.com/v3/place/text?parameters";
	public final static int DEFAULT_GAODE_PAGE_SIZE=1; //每页返回1条信息，默认地图的第一条记录就是最接近的地址
	public final static String DEFAULT_GAODE_FORWARD_GEOCODE_URL="https://restapi.amap.com/v3/geocode/geo?parameters";
	public final static String DEFAULT_GAODE_REVERSE_GEOCODE_URL="https://restapi.amap.com/v3/geocode/regeo?parameters";
	//高德地图接口调用每天次数限制
	public final static long GAODE_POI_SEARCH_TIMES=30000;
	public final static long GAODE_GEO_REV_GEO_TIMES=300000;
	public final static long GAODE_COOR_TRANS_TIMES=300000;
	
	//地图接口城市限定，以及相似度阈值
//	public final static String CITY_NAME="汕头";								//限定接口的访问城市
	public final static Float SEMANTIC_SIMILAR=(float) 0.1;			//限定语义相似度过滤的阈值，允许拿较低的相似度地址进行尝试

}
