package com.example.util;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;
import java.util.TreeSet;

/**
 * 提供对同个网格中的分光器进行最小凸包的生成
 * @author chinamobile
 *
 */
public class GrahamScan {
	public static enum Turn { CLOCKWISE, COUNTER_CLOCKWISE, COLLINEAR, ERRORCODE }
	

	/**
	 * 主入口方法1：针对点集合，进行凸包的Graham扫描算法生成多边形
	 * @param polygonPoints 点集合
	 * @return
	 */
	public static List<Point2D.Double> getConvexHullByStringList(List<String> polygonPoints){
		List<Point2D.Double> pointsList=null;
		Point2D.Double point=null;
		String[] gps=null;
		if(polygonPoints==null||polygonPoints.size()<3)return null;
		pointsList=new ArrayList<Point2D.Double>();
		for(int i=0;i<polygonPoints.size();i++) {
			gps=polygonPoints.get(i).split(",");
			point=new Point2D.Double(Double.valueOf(gps[0]).doubleValue(), Double.valueOf(gps[1]).doubleValue());
			pointsList.add(point);
		}
		pointsList=getConvexHull(pointsList);
		return pointsList;
	}
	
	/**
	 * 主入口方法2：针对点集合，进行凸包的Graham扫描算法生成多边形
	 * @param pointsList 点集合
	 * @return
	 */
	public static List<Point2D.Double> getConvexHull(List<Point2D.Double> pointsList){
		List<Point2D.Double> convexHull=null;
		List<Point2D.Double> pointsTreeList=null;
		TreeSet<Point2D.Double> pointsTreeSet=null;
		boolean flag=false;
		
		Stack<Point2D.Double> stack=null;
		try {
			//1.先对点去重排序
			pointsTreeSet=getSortedPointTreeSet(pointsList);
			if(pointsTreeSet!=null&&pointsTreeSet.size()>3) {
				pointsTreeList=new ArrayList<Point2D.Double>();
				pointsTreeList.addAll(pointsTreeSet); //将TreeSet转为List
				
				//2.判断点是否共线
				flag=areAllPointsCollinear(pointsTreeList);
				if(flag==false) { //3.在满足1,2的条件下，开始进行Graham扫描模型
					convexHull=new ArrayList<Point2D.Double>();
					stack=new Stack<Point2D.Double>();
					
					//3.1 先将最开始两个点压入堆栈
					stack.push(pointsTreeList.get(0));
					stack.push(pointsTreeList.get(1));
					
					//3.2 Graham扫描组合出多边形
					for(int i=2;i<pointsTreeList.size();i++) {
						Point2D.Double head = pointsTreeList.get(i);
						Point2D.Double middle = stack.pop();
						Point2D.Double tail = stack.peek();

			            Turn turn = getTurn(tail, middle, head);
			            if(convexHull==null) {
			            	break;
			            }

			            switch(turn) {
			                case COUNTER_CLOCKWISE:
			                    stack.push(middle);
			                    stack.push(head);
			                    break;
			                case CLOCKWISE:
			                    i--;
			                    break;
			                case COLLINEAR:
			                    stack.push(head);
			                    break;
							default:
								convexHull=null;
								break;
			            }
					}
					
					if(convexHull!=null) {
						stack.push(pointsTreeList.get(0));//形成凸包的闭包
						convexHull.addAll(stack);
					}
				}
			}	
		}catch(Exception ex) {
			convexHull=null;
		}
		
		return convexHull;
	}
	
	/**
	 * 获得底部顶点后，对多边形的点按照方向角的大小进行排序，便于后续获取多边形时进行Graham扫描
	 * 方法返回结果是TreeSet能够有效去除重复点，并且最低点已经排在TreeSet的第一个位置
	 * @param pointsList 点集合
	 * @return
	 */
	public static TreeSet<Point2D.Double> getSortedPointTreeSet(List<Point2D.Double> pointsList){
		TreeSet<Point2D.Double> pointSet=null;
		if(pointsList==null||pointsList.size()<=2)return null;
		try {
			final Point2D.Double lowestPoint=getLowestPoint(pointsList); //先获取最底部的起点,基本保证了向量的角度在0到180度之间
			pointSet=new TreeSet<Point2D.Double>(new Comparator<Point2D.Double>() {
				@Override
				public int compare(Point2D.Double a, Point2D.Double b) {
					if(a == b || a.equals(b)) {
	                    return 0;
	                }
					double thetaA = Math.atan2(a.y - lowestPoint.y, a.x - lowestPoint.x);
		            double thetaB = Math.atan2(b.y - lowestPoint.y, b.x - lowestPoint.x);
					
		            if(thetaA < thetaB) {
	                    return -1;
	                }
	                else if(thetaA > thetaB) {
	                    return 1;
	                }else {
	                	double distanceA = Math.sqrt(((lowestPoint.x - a.x) * (lowestPoint.x - a.x)) +
                                ((lowestPoint.y - a.y) * (lowestPoint.y - a.y)));
	                	double distanceB = Math.sqrt(((lowestPoint.x - b.x) * (lowestPoint.x - b.x)) +
                                ((lowestPoint.y - b.y) * (lowestPoint.y - b.y)));
	                	
	                	if(distanceA < distanceB) {
	                        return -1;
	                    }
	                    else if(distanceA > distanceB) {
	                        return 1;
	                    }else{
	                    	return 0;
	                    }
	                }
				}
			});
			
			pointSet.addAll(pointsList);
		} catch (Exception ex) {
			pointSet=null;
		}
		return pointSet;
	}
	
	/**
	 * 获取多边形中位于y轴最底部的点
	 * @param pointsList
	 * @return
	 */
	public static Point2D.Double getLowestPoint(List<Point2D.Double> pointsList) {
		Point2D.Double lowestPoint=null;
		if(pointsList==null||pointsList.size()<=2)return null;
		try {
			lowestPoint=pointsList.get(0);
			for(int i=1;i<pointsList.size();i++) {
				Point2D.Double tmpPoint=pointsList.get(i);
				if(tmpPoint.y<lowestPoint.y||(tmpPoint.y==lowestPoint.y&&tmpPoint.x<lowestPoint.x)) {
					lowestPoint=tmpPoint;
				}
			}
		}catch(Exception ex) {
			lowestPoint=null;
		}
		return lowestPoint;
	}
	
	
	/**
	 * 判断点集合是否是全部共线，包含对点数量的判别，至少3点才能构成多边形
	 * @param pointsList
	 * @return false说明不共线，true可能共线也可能是有其他报错，建议不做处理
	 */
	public static boolean areAllPointsCollinear(List<Point2D.Double> pointsList) {
		boolean flag=true;
		Point2D.Double a=null;
		Point2D.Double b=null;
		Point2D.Double c=null;
		try {
			if(pointsList!=null&&pointsList.size()>2){
				a=pointsList.get(0);
				b=pointsList.get(1);
				for(int i=2;i<pointsList.size();i++) {
					c=pointsList.get(i);
					if(getTurn(a, b, c) != Turn.COLLINEAR&&getTurn(a, b, c) != Turn.ERRORCODE) {
		                flag=false;
		                break;
		            }
				}
			}
		}catch(Exception ex) {
		}
		a=null;
		b=null;
		c=null;
		return flag;
	}
	
	/**
	 * 判断3点之间的关系，顺时针，逆时针，还是共线
	 * @param a a是起点
	 * @param b 是第一个向量ab的终点
	 * @param c 是第二个向量bc的终点
	 * @return
	 */
	public static Turn getTurn(Point2D.Double a, Point2D.Double b, Point2D.Double c) {
		if(a==null||b==null||c==null)return Turn.ERRORCODE;
		
		double crossProduct=(b.x-a.x)*(c.y-a.y)-(c.x-a.x)*(b.y-a.y);
		
		if(crossProduct > 0) {//说明c在ab的逆时针方向
			return Turn.COUNTER_CLOCKWISE;
		}else if(crossProduct < 0) { //说明c在ab的顺时针方向
			return Turn.CLOCKWISE;
		}
		//其余情况就是共线的情况了
		return Turn.COLLINEAR;
	}
	
	/**
	 * 测试代码
	 * @param args
	 */
	public static void main(String[] args) {
		String pList="116.65487,23.39202;116.6558069,23.39033482;116.6548476,23.39162962;116.6543577,23.39189415;116.6543577,23.39189415;116.6543577,23.39189415;116.6543577,23.39189415;116.6543577,23.39189415;116.6543577,23.39189415;116.6543577,23.39189415;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.655304,23.3913;116.656051,23.38944624;116.6548723,23.39164515;116.6548723,23.39164515";
		String[] points=null;
		String[] point=null;
		List<Point2D.Double> pointsList=new ArrayList<Point2D.Double>();
		List<Point2D.Double> pointsList2=new ArrayList<Point2D.Double>();
		Point2D.Double point2d=null;
//		TreeSet<Point2D.Double> pointSet=null;
//		boolean flag=false;
		
		points=pList.split(";");
		for(int i=0;i<points.length;i++) {
			point=points[i].split(",");
			point2d=new Point2D.Double(Double.valueOf(point[0]).doubleValue(), Double.valueOf(point[1]).doubleValue());
			pointsList.add(point2d);
		}
		
		pointsList2=GrahamScan.getConvexHull(pointsList);
		System.out.println("====================================");
		for(int i=0;i<pointsList2.size();i++) {
			System.out.println(pointsList2.get(i).x+","+pointsList2.get(i).y);
		}
		
		//下列代码证明，逆时针排序后，最低点位于数组的第一个位置
		//必须先做排序，去重再判断点是否共线
//		pointSet=GrahamScan.getSortedPointTreeSet(pointsList);
//		
//		pointsList2.addAll(pointSet);
//		flag=GrahamScan.areAllPointsCollinear(pointsList2);
//		System.out.println(flag);

//		
//		Iterator<Point2D.Double> it = pointSet.iterator();  
//		while (it.hasNext()) {  
//			point2d = it.next();  
//			System.out.println(point2d.x+","+point2d.y);
//		}  
//		System.out.println("=======================================");
//		point2d=GrahamScan.getLowestPoint(pointsList);
//		System.out.println(point2d.x+","+point2d.y);
		
	}
}
