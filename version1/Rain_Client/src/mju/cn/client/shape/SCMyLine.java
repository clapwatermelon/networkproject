package mju.cn.client.shape;

import java.awt.Color;
import java.awt.geom.Line2D;


public class SCMyLine extends Line2D.Double implements SCMyShape {
	public SCMyLine(double x1, double y1, double x2, double y2, Color c,
			int size) {
		super(x1, y1, x2, y2);
		color = c;
		pointSize = size;
	}

	public Color getColor() {
		return color;
	}

	public int getPointSize() {
		return pointSize;
	}

	private Color color;
	private int pointSize;
}
