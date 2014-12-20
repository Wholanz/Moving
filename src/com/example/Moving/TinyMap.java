package com.example.Moving;


import java.util.Random;

import android.graphics.Point;

public class TinyMap {
	private static final int line = 21;//行数
	private static final int row = 16;//列数
	
	Random rand;
	
	private Point start, end;
	
	private int[][] m;
	private int difficult;
	private int b_size;
	
	private int x0, y0;
	
	public TinyMap( int dif, int g,  int size)
	{
		m = new  int[line][row];
		start = new Point();
		end = new Point();
		for(int i = 0;i < line;i++)
			for(int j = 0;j < row;j++)
			{
				m[i][j] = -1;
			}
		difficult = dif;
		b_size = size;
		rand = new Random();
		start.y = 0;
		start.x = rand.nextInt(line);
		end.x = line - start.x > 0 ?line - start.x : - line + start.x ;
		end.y = (row  - 1);
		m[start.x][start.y] = 0;
		m[end.x][end.y] = 3;
		

		buildwall(g);
		buildmap();
	}
	
	private void buildwall(int g)
	{
		switch(g)
		{
		case 1:
			for(int i = 0; i  <  10;i++)
			{
				m[5][3 + i] = 1;
				m[15][3 + i] = 1;
			}
			break;
		case 2:
			for(int i = 0; i  <  6;i++)
			{
				m[4 + i][5] = 1;
				m[4 + i][10] = 1;
				m[14][5 + i] = 1;;
			}
			break;
		case 3:
			for(int i = 0; i  <  6;i++)
			{
				m[8 + i][5] = 1;
				m[8 + i][10] = 1;
				m[16][5 + i] = 1;
				m[5][5 + i] = 1;
			}
			break;
		case 4 :
			for(int i = 0; i  <  6;i++)
			{
				m[8 + i][5] = 1;
				m[8 + i][10] = 1;
				m[15][5 + i] = 1;
				m[5][5 + i] = 1;
				m[8 + i][2] = 1;
				m[8 + i][13] = 1;

			}
			break;
		case 5:
			for(int i = 0; i  <  6;i++)
			{
				m[8 + i][5] = 1;
				m[8 + i][10] = 1;
				m[16][5 + i] = 1;
				m[5][5 + i] = 1;
				m[8 + i][2] = 1;
				m[8 + i][13] = 1;
				m[2][5 + i] = 1;
				m[18][5 + i] = 1;
			}
			break;
		case 6:
			for(int i = 2; i  < 19 ; i++)
			{
				if(i % 3 == 0)
				{
					for(int j = 2;j < 13;j++)
					{
						if(i != j)
							m[i][j] = 1;
					}
				}
			}
			break;
		}
	}
	
	
	private void buildmap()
	{
		int  x, y; 
		int t;
		int d = difficult;
		while(d > 0)
		{
			//获取随机坐标
			x = rand.nextInt(line);
			y = rand.nextInt(row);
			if(m[x][y] != -1  || x >= line || x < 0 || y >= row || y < 0)
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
			m[x][y] = t;
			d--;
		}
		//其他地方填充为道路
		for(int i = 0;i < line;i++)
			for(int j = 0;j < row;j++)
			{
				if(m[i][j] == -1)
					m[i][j] = 0;
			}
		return;
	}
	//返回地图数组
	public int[][] getArray()
	{
		return m;
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
		if(right > b_size * row || left < x0)
			return 2;
		if(top < y0 || bottom > b_size * line)
			return 1;
		int l = left / b_size, t = top / b_size, r = right / b_size, b = bottom / b_size;
		int x = (left + right) / 2 / b_size;
		int y = (top + bottom) /  2 / b_size;
		
		//if (m[l][t] == 1 || m[l][b] == 1 || m[r][t] == 1 || m[r][b] == 1//方块四角
					//|| m[x][t] == 1 || m[x][b] == 1 ||m [l][y] == 1 || m[r][y] == 1);//各边中点

		try{
				if(m[t][l] == 1 ||m[t][r] == 1 ||m [y][l] == 1 || m[y][r] == 1) return 2;
			if(m[b][l] == 1 ||m[b][r] == 1|| m[t][x] == 1 || m[b][x] == 1 )return 1;
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
			return (m[c_y / b_size][c_x / b_size] == 2);
		}
		catch(ArrayIndexOutOfBoundsException e){
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
			return (m[c_y / b_size][c_x / b_size] == 3);
		}
		catch(ArrayIndexOutOfBoundsException e){
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
				if(m[i][j] == 0)
					s += "" + " ";
				else 
					s += 0 + " ";
			}
			s += "\n";
		}
		return s;
	}
}
	

