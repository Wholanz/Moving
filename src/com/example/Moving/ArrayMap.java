package com.example.Moving;


import java.util.Random;

import android.graphics.Point;

public class ArrayMap {
	private static final int line = 21;//行数
	private static final int row = 16;//列数
	
	Random rand;
	
	private Point start, end;
	
	private int[][] savedArray;
	private int difficult;
	private int b_size;
	
	private int x0, y0;
	
	public ArrayMap( int dif, int g,  int size)
	{
		savedArray = new  int[line][row];
		start = new Point();
		end = new Point();
		for(int i = 0;i < line;i++)
			for(int j = 0;j < row;j++)
			{
				savedArray[i][j] = -1;
			}
		difficult = dif;
		b_size = size;
		rand = new Random();
		start.y = 0;
		start.x = rand.nextInt(line);
		end.x = line - start.x > 0 ?line - start.x : - line + start.x ;
		end.y = (row  - 1);
		savedArray[start.x][start.y] = 0;
		savedArray[end.x][end.y] = 3;
		

		buildwall(g);
		buildMap();
	}
	
	private void buildwall(int level)
	{
		switch(level)
		{
		case 1:
			for(int i = 0; i  <  7;i++)
			{
				savedArray[7][i + 4] = 1;
				savedArray[i + 2][9] = 1;
				savedArray[15][i + 5] = 1;
			}
			break;
		case 2:
			for(int i = 0; i  <  7;i++)
			{
				savedArray[i + 4][4] = 1;
				savedArray[i + 8][10] = 1;
				savedArray[15][i + 5] = 1;
			}
			break;
		case 3:
			for(int i = 0; i  <  8;i++)
			{
				savedArray[10][i + 4] = 1;
				savedArray[i + 3][8] = 1;
				savedArray[15][i + 6] = 1;
			}
			break;
		case 4 :
			for(int i = 0; i  <  8;i++)
			{
				savedArray[i + 4][10] = 1;
				savedArray[ 3][7 + i] = 1;
				savedArray[15][i + 6] = 1;
			}
			break;
		case 5:
			for(int i = 0; i  <  9;i++)
			{
				savedArray[5][i] = 1;
				savedArray[i + 6][8] = 1;
				savedArray[18][i + 6] = 1;
			}
			break;
		case 6:
			for(int i = 0; i  <  9;i++)
			{
				savedArray[10 + i][3] = 1;
				savedArray[i + 3][8] = 1;
				savedArray[18][i + 6] = 1;
			}
			break;
		}
	}
	
	
	private void buildMap()
	{
		int  x, y; 
		int t;
		int d = difficult;
		while(d > 0)
		{
			//获取随机坐标
			x = rand.nextInt(line);
			y = rand.nextInt(row);
			if(savedArray[x][y] != -1  || x >= line || x < 0 || y >= row || y < 0)
				continue;
			//获取该块类型
			/***********
			 int r
			r = rand.nextInt(2);
			switch(r)
			{
			case 0:
				t = 2;
				break;
			case 1:
				t = 1;
				break;
			default:
				continue;
			}
			************/
			t = 2;
			savedArray[x][y] = t;
			d--;
		}
		//其他地方填充为道路
		for(int i = 0;i < line;i++)
			for(int j = 0;j < row;j++)
			{
				if(savedArray[i][j] == -1)
					savedArray[i][j] = 0;
			}
		return;
	}
	//返回地图数组
	public int[][] getArray()
	{
		return savedArray;
	}
	
	public void setPara(int left, int top)
	{
		x0 = left;
		y0 = top;
	}
	
	
	//返回起点与终点
	public Point getStart()
	{
		return start;
	}
	public Point getEnd()
	{
		return end;
	}
	
	public int isCollision(int left, int top, int right, int bottom)
	{
		left -= x0;right -= x0;
		top -= y0;bottom -= y0;
		
		int l = left / b_size, t = top / b_size, r = right / b_size, b = bottom / b_size;
		int x = (left + right) / 2 / b_size;
		int y = (top + bottom) /  2 / b_size;
		
		//if (savedArray[l][t] == 1 || savedArray[l][b] == 1 || savedArray[r][t] == 1 || savedArray[r][b] == 1//方块四角
					//|| savedArray[x][t] == 1 || savedArray[x][b] == 1 ||savedArray [l][y] == 1 || savedArray[r][y] == 1);//各边中点
		try{
				if(savedArray[t][l] == 1 || savedArray[t][r] == 1 || savedArray[y][l] == 1 || savedArray[y][r] == 1) return 2;
			if(savedArray[b][l] == 1 || savedArray[b][r] == 1|| savedArray[t][x] == 1 || savedArray[b][x] == 1 )return 1;
			else return 0;
		}
		catch(Exception e){
			return 0;
		}
	}
	public boolean isDead(int left, int top, int right, int bottom)
	{
		left -= x0;right -= x0;
		top -= y0;bottom -= y0;
		
		int c_x = (left + right) / 2;
		int c_y = (top + bottom) /  2;

		try{
			return (savedArray[c_y / b_size][c_x / b_size] == 2);
		}
		catch(Exception e){
			return false;
		}
	}
	public boolean isArrive(int left, int top, int right, int bottom)
	{
		left -= x0;right -= x0;
		top -= y0;bottom -= y0;
		
		int c_x = (left + right) / 2;
		int c_y = (top + bottom) /  2;

		try{
			return (savedArray[c_y / b_size][c_x / b_size] == 3);
		}
		catch(Exception e){
			return false;
		}
		
	}
	
	public String toString()
	{
		String s = "";
		for(int i = 0;i < line;i++)
		{
			for(int j = 0;j < row;j++)
			{
				if(savedArray[i][j] == 0)
					s += "" + " ";
				else 
					s += 0 + " ";
			}
			s += "\n";
		}
		return s;
	}
}
	

