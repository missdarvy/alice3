/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.lgna.ik.poser.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import org.lgna.croquet.BooleanState;
import org.lgna.croquet.components.BooleanStateButton;
import org.lgna.croquet.components.CustomRadioButtons;
import org.lgna.ik.poser.TimeLineComposite;
import org.lgna.ik.poser.TimeLineComposite.PoseEvent;
import org.lgna.ik.poser.events.TimeLineListener;

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;

class TimeLineLayout implements LayoutManager {

	private final TimeLineComposite timeLine;

	public TimeLineLayout( TimeLineComposite masterComposite ) {
		super();
		this.timeLine = masterComposite;
	}

	public void layoutContainer( Container parent ) {
		for( Component child : parent.getComponents() ) {
			int x = 100;
			if( child instanceof JTimeLinePoseMarker ) {
				JTimeLinePoseMarker jMarker = (JTimeLinePoseMarker)child;
				x = getXforTime( jMarker.getTimeLinePoseMarker().getItem().getEventTime(), parent );
				x = makeCenter( jMarker, x );
			}
			child.setLocation( x, 0 );
			child.setSize( child.getPreferredSize() );
			child.repaint();
		}
	}

	private int makeCenter( JTimeLinePoseMarker jMarker, int x ) {
		return x - ( jMarker.getWidth() / 2 );
	}

	public int getXforTime( int time, Container parent ) {
		return (int)( ( (double)( time - timeLine.getStartTime() ) / ( (double)timeLine.getEndTime() - timeLine.getStartTime() ) )
				* ( getXMax() - getXMin() ) ) + getXMin();
	}

	private int getXMin() {
		return timeLine.getViewWidth() / 24;
	}

	private int getXMax() {
		return timeLine.getViewWidth() - ( timeLine.getViewWidth() / 24 );
	}

	public Dimension minimumLayoutSize( Container parent ) {
		Component[] children = parent.getComponents();
		int width = 0;
		int height = 0;
		for( Component component : children ) {
			if( component.getMinimumSize().width > width ) {
				width = component.getMinimumSize().width;
			}
			if( component.getMinimumSize().height > height ) {
				height = component.getMinimumSize().height;
			}
		}
		return new Dimension( width, height );
	}

	public Dimension preferredLayoutSize( Container parent ) {
		Component[] children = parent.getComponents();
		int width = 0;
		int height = 0;
		for( Component component : children ) {
			if( component.getPreferredSize().width > width ) {
				width = component.getPreferredSize().width;
			}
			if( component.getPreferredSize().height > height ) {
				height = component.getPreferredSize().height;
			}
		}
		return new Dimension( width, height );
	}

	public void removeLayoutComponent( Component comp ) {
	}

	public void addLayoutComponent( String name, Component comp ) {
	}

}

/**
 * @author Matt May
 */

public class TimeLineView extends CustomRadioButtons<PoseEvent> {

	private class JTimeLineView extends JItemSelectablePanel {
		private final TimeLineComposite timeLine;
		private final TimeLineListener timeLineListener = new TimeLineListener() {
			public void changed() {
				repaint();
			}
		};

		public int getTimeFromLocation( int x ) {
			int time = (int)( ( (double)( x - getXMin() ) / ( getXMax() - getXMin() ) ) * ( timeLine.getEndTime() - timeLine.getStartTime() ) );
			if( time < timeLine.getStartTime() ) {
				return timeLine.getStartTime();
			}
			if( time > timeLine.getEndTime() ) {
				return timeLine.getEndTime();
			}
			return time;
		}

		public int getXforTime( int time ) {
			return (int)( ( (double)( time - timeLine.getStartTime() ) / ( (double)timeLine.getEndTime() - timeLine.getStartTime() ) )
					* ( getXMax() - getXMin() ) ) + getXMin();
		}

		private int getXMin() {
			return getWidth() / 24;
		}

		private int getXMax() {
			return getWidth() - ( getWidth() / 24 );
		}

		public JTimeLineView( TimeLineComposite timeLine ) {
			this.timeLine = timeLine;
			this.timeLine.addTimeLineListener( this.timeLineListener );
		}

		@Override
		public Dimension getPreferredSize() {
			return DimensionUtilities.constrainToMinimumHeight( super.getPreferredSize(), 100 );
		}

		@Override
		public void paintComponent( Graphics g ) {
			super.paintComponent( g );
			g.setColor( Color.RED );
			g.fillRoundRect( 0, 0, this.getWidth(), this.getHeight(), this.getHeight() / 4, this.getHeight() / 4 );
			g.setColor( Color.BLACK );
			g.drawLine( getXMin(), (int)( getHeight() * .5 ), getXMax(), (int)( getHeight() * .5 ) );
			g.drawLine( getXMin(), (int)( ( getHeight() * .5 ) + ( getHeight() / 16 ) ), getXMin(), (int)( ( getHeight() * .5 ) - ( getHeight() / 16 ) ) );
			g.drawLine( getXMax(), (int)( ( getHeight() * .5 ) + ( getHeight() / 16 ) ), getXMax(), (int)( ( getHeight() * .5 ) - ( getHeight() / 16 ) ) );
			Point[] pArr = this.getPointsForSlider();
			int[] xArr = new int[ pArr.length ];
			int[] yArr = new int[ pArr.length ];
			for( int i = 0; i != pArr.length; ++i ) {
				xArr[ i ] = pArr[ i ].x;
				yArr[ i ] = pArr[ i ].y;
			}
			g.fillPolygon( xArr, yArr, pArr.length );
			for( PoseEvent o : timeLine.getPosesInTimeline() ) {
				int x = getXforTime( o.getEventTime() );
				g.drawLine( x, 0, x, getHeight() );
			}
		}

		private Point[] getPointsForSlider() {
			Point arrow = new Point( getXforTime( timeLine.getCurrentTime() ), ( this.getHeight() ) / 2 );
			Point leftBase = new Point( arrow.x + ( this.getWidth() / 32 ), ( this.getHeight() * 3 ) / 4 );
			Point rightBase = new Point( arrow.x - ( this.getWidth() / 32 ), ( this.getHeight() * 3 ) / 4 );
			Point[] rv = { arrow, leftBase, rightBase };
			return rv;
		}

		public void updateSlider( MouseEvent e ) {
			this.timeLine.setCurrentTime( getTimeFromLocation( e.getLocationOnScreen().x ) );
		}

		public boolean isClickingOnSlider( Point locationOnScreen ) {
			return true;
		}
	}

	private TimeLineComposite composite;

	public TimeLineView( TimeLineComposite composite ) {
		super( composite.getPoseEventListSelectionState() );
		this.composite = composite;
		composite.setJComponent( this );
	}

	@Override
	protected LayoutManager createLayoutManager( JPanel jPanel ) {
		return new TimeLineLayout( composite );
	}

	@Override
	protected void addPrologue( int count ) {
	}

	@Override
	protected void addItem( PoseEvent item, BooleanStateButton<?> button ) {
		this.internalAddComponent( button );
	}

	@Override
	protected void addEpilogue() {
	}

	@Override
	protected BooleanStateButton<?> createButtonForItemSelectedState( PoseEvent item, BooleanState itemSelectedState ) {
		return new TimeLinePoseMarker( itemSelectedState, item );
	}

	@Override
	protected void removeAllDetails() {
		this.internalRemoveAllComponents();
	}

	@Override
	protected JPanel createJPanel() {
		return new JTimeLineView( composite );
	}

}
