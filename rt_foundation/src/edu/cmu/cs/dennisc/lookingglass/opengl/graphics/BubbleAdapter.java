package edu.cmu.cs.dennisc.lookingglass.opengl.graphics;

public abstract class BubbleAdapter<E extends edu.cmu.cs.dennisc.scenegraph.graphics.Bubble> extends ShapeEnclosedTextAdapter< E > {
	private java.awt.geom.Point2D.Float originOfTail = new java.awt.geom.Point2D.Float();
	private java.awt.geom.Point2D.Float bodyConnectionLocationOfTail = new java.awt.geom.Point2D.Float();
	private java.awt.geom.Point2D.Float textBoundsOffset = new java.awt.geom.Point2D.Float();
	protected abstract void render( 
			edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, 
			edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, 
			java.awt.Rectangle actualViewport, 
			edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, 
			String text, 
			java.awt.Font font, 
			java.awt.geom.Rectangle2D textBounds,
			java.awt.Color textColor, 
			java.awt.Color fillColor, 
			java.awt.Color outlineColor,
			java.awt.geom.Point2D.Float originOfTail,
			java.awt.geom.Point2D.Float bodyConnectionLocationOfTail,
			java.awt.geom.Point2D.Float textBoundsOffset,
			double portion );
	@Override
	protected void render( edu.cmu.cs.dennisc.lookingglass.Graphics2D g2, edu.cmu.cs.dennisc.lookingglass.LookingGlass lookingGlass, java.awt.Rectangle actualViewport, edu.cmu.cs.dennisc.scenegraph.AbstractCamera camera, java.lang.String text, java.awt.Font font, java.awt.geom.Rectangle2D textBounds,
			java.awt.Color textColor, java.awt.Color fillColor, java.awt.Color outlineColor ) {
		
		edu.cmu.cs.dennisc.scenegraph.graphics.Bubble.Originator originator = this.m_element.originator.getValue();
		if( originator != null ) {
			originator.calculate( originOfTail, bodyConnectionLocationOfTail, textBoundsOffset, this.m_element, lookingGlass, actualViewport, camera, textBounds );
			this.render( g2, lookingGlass, actualViewport, camera, text, font, textBounds, textColor, fillColor, outlineColor, originOfTail, bodyConnectionLocationOfTail, textBoundsOffset, m_element.portion.getValue() );
		}
	}
	@Override
	protected void propertyChanged( edu.cmu.cs.dennisc.property.InstanceProperty< ? > property ) {
		if( property == m_element.originator ) {
			//pass
		} else if( property == m_element.portion ) {
			//pass
		} else {
			super.propertyChanged( property );
		}
	}
}
