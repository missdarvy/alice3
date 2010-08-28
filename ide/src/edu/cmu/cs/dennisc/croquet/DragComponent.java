/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
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
package edu.cmu.cs.dennisc.croquet;

public abstract class DragComponent extends Control {
	private DragAndDropOperation dragAndDropOperation;

	private DragProxy dragProxy = null;
	private DropProxy dropProxy = null;

	private java.awt.event.ComponentListener componentAdapter = new java.awt.event.ComponentListener() {
		public void componentHidden( java.awt.event.ComponentEvent arg0 ) {
		}
		public void componentMoved( java.awt.event.ComponentEvent e ) {
		}
		public void componentResized( java.awt.event.ComponentEvent e ) {
			DragComponent.this.updateProxySizes();
		}
		public void componentShown( java.awt.event.ComponentEvent e ) {
		}
	};

	public DragComponent() {
		this.updateProxySizes();
	}

//	public Resolver< DragAndDropOperation > getDragAndDropOperationResolver() {
//		return new Resolver< DragAndDropOperation >() {
//			public edu.cmu.cs.dennisc.croquet.DragAndDropOperation getResolved() {
//				DragComponent.this.dragAndDropOperation.setFirstComponentHint( DragComponent.this );
//				return DragComponent.this.dragAndDropOperation;
//			}
//		};
//	}

	/*package-private*/ DragProxy getDragProxy() {
		return this.dragProxy;
	}
	/*package-private*/ DropProxy getDropProxy() {
		return this.dropProxy;
	}
	
	public java.awt.Dimension getDropProxySize() {
		return this.dropProxy.getSize();
	}

	@Override
	public void addNotify() {
		super.addNotify();
		this.addComponentListener( this.componentAdapter );
	}
	@Override
	public void removeNotify() {
		this.removeComponentListener( this.componentAdapter );
		super.removeNotify();
	}

	@Override
	protected boolean isMouseListeningDesired() {
		return super.isMouseListeningDesired() || this.dragAndDropOperation != null;
	}

	public Component<?> getSubject() {
		return this;
	}
	//	public void setSubject( java.awt.Component subject ) {
	//		this.subject = subject;
	//	}
	public DragAndDropOperation getDragAndDropOperation() {
		if( this.dragAndDropOperation != null ) {
			this.dragAndDropOperation.setFirstComponentHint( DragComponent.this );
		}
		return this.dragAndDropOperation;
	}
	public void setDragAndDropOperation( DragAndDropOperation dragAndDropOperation ) {
		if( this.dragAndDropOperation != dragAndDropOperation ) {
//			if( this.dragAndDropOperation != null ) {
//				assert dragAndDropOperation == null; 
//			} else {
//				assert dragAndDropOperation != null; 
//			}
			if( this.dragAndDropOperation != null ) {
				this.dragAndDropOperation.removeComponent( this );
			}
			this.dragAndDropOperation = dragAndDropOperation;
			if( this.dragAndDropOperation != null ) {
				this.dragAndDropOperation.addComponent( this );
			}
		} else {
			assert false;
		}
	}

	protected boolean isAlphaDesiredWhenOverDropReceptor() {
		return false;
	}

	private boolean isActuallyPotentiallyDraggable() {
		boolean rv = this.dragAndDropOperation != null;
		if( rv ) {
			if( this.dragProxy != null ) {
				//pass
			} else {
				this.dragProxy = new DragProxy( this, this.isAlphaDesiredWhenOverDropReceptor() );
			}
			if( this.dropProxy != null ) {
				//pass
			} else {
				this.dropProxy = new DropProxy( this );
			}
		}
		return rv;
	}

	protected javax.swing.JLayeredPane getLayeredPane() {
		javax.swing.JRootPane rootPane = this.getRoot().getRootPane();
		if( rootPane != null ) {
			return rootPane.getLayeredPane();
		} else {
			throw new RuntimeException( "cannot find rootPane: " + this );
			//return null;
		}
	}

	private void updateProxySizes() {
		if( isActuallyPotentiallyDraggable() ) {
			dragProxy.setSize( dragProxy.getProxyWidth(), dragProxy.getProxyHeight() );
			dropProxy.setSize( dropProxy.getProxyWidth(), dropProxy.getProxyHeight() );
		}
	}
	private synchronized void updateProxyPosition( java.awt.event.MouseEvent e ) {
		if( isActuallyPotentiallyDraggable() ) {
			java.awt.event.MouseEvent mousePressedEvent = this.getLeftButtonPressedEvent();
			if( mousePressedEvent != null ) {
				javax.swing.JLayeredPane layeredPane = getLayeredPane();
				java.awt.Point locationOnScreenLayeredPane = layeredPane.getLocationOnScreen();
				java.awt.Point locationOnScreen = this.getLocationOnScreen();
				int dx = locationOnScreen.x - locationOnScreenLayeredPane.x;
				int dy = locationOnScreen.y - locationOnScreenLayeredPane.y;

				dx -= mousePressedEvent.getX();
				dy -= mousePressedEvent.getY();

				boolean isCopyDesired = edu.cmu.cs.dennisc.javax.swing.SwingUtilities.isQuoteControlUnquoteDown( e );
				int x = e.getX() + dx;
				int y = e.getY() + dy;
				dragProxy.setCopyDesired( isCopyDesired );
				dragProxy.setLocation( x, y );
				//layeredPane.setPosition( dragProxy, dy );
				dropProxy.setCopyDesired( isCopyDesired );
			}
		}
	}


	private DragAndDropContext dragAndDropContext;

	private void handleLeftMouseDraggedOutsideOfClickThreshold( java.awt.event.MouseEvent e ) {
		Application application = Application.getSingleton();
		application.setDragInProgress( true );
		ModelContext<?> parentContext = RootContext.getInstance().getCurrentContext();
	
		this.updateProxySizes();
		this.updateProxyPosition( e );

		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		layeredPane.add( this.dragProxy, new Integer( 1 ) );
		layeredPane.setLayer( this.dragProxy, javax.swing.JLayeredPane.DRAG_LAYER );

		this.dragAndDropContext = parentContext.createDragAndDropContext( this.dragAndDropOperation, this.getLeftButtonPressedEvent(), e, this );
		this.dragAndDropOperation.handleDragStarted( this.dragAndDropContext );
	}
	private void handleLeftMouseDragged( java.awt.event.MouseEvent e ) {
		if( Application.getSingleton().isDragInProgress() ) {
			this.updateProxyPosition( e );
			this.dragAndDropContext.handleMouseDragged( e );
		}
	}
	@Override
	protected void handleMouseDraggedOutsideOfClickThreshold( java.awt.event.MouseEvent e ) {
		super.handleMouseDraggedOutsideOfClickThreshold( e );
		if( isActuallyPotentiallyDraggable() ) {
			if(edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
				this.handleLeftMouseDraggedOutsideOfClickThreshold( e );
			}
		}
	}
	@Override
	public void handleMouseDragged( java.awt.event.MouseEvent e ) {
		super.handleMouseDragged( e );
		if( isActuallyPotentiallyDraggable() ) {
			if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "isActuallyPotentiallyDraggable == true" );
				if( this.isWithinClickThreshold() ) {
					//pass
				} else {
					this.handleLeftMouseDragged( e );
				}
			}
		}
	}

	@Override
	public void handleMousePressed( java.awt.event.MouseEvent e ) {
		super.handleMousePressed( e );
		if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteRightUnquoteMouseButton( e ) ) {
			if( Application.getSingleton().isDragInProgress() ) {
				this.handleCancel( e );
			}
		}
	}
	@Override
	public void handleMouseReleased( java.awt.event.MouseEvent e ) {
		if( isActuallyPotentiallyDraggable() ) {
			if( edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities.isQuoteLeftUnquoteMouseButton( e ) ) {
				if( this.isWithinClickThreshold() ) {
					//pass
				} else {
					Application.getSingleton().setDragInProgress( false );
					this.setActive( this.getAwtComponent().contains( e.getPoint() ) );
					javax.swing.JLayeredPane layeredPane = getLayeredPane();
					java.awt.Rectangle bounds = this.dragProxy.getBounds();
					layeredPane.remove( this.dragProxy );
					layeredPane.repaint( bounds );
					if( this.dragAndDropContext != null ) {
						this.dragAndDropContext.handleMouseReleased( e );
					}
				}
			}
		}
	}

	public void showDragProxy() {
		this.dragProxy.setVisible( true );
	}
	public void hideDragProxy() {
		this.dragProxy.setVisible( false );
	}
	public void setDropProxyLocationAndShowIfNecessary( java.awt.Point p, Component<?> asSeenBy, Integer heightToAlignLeftCenterOn, int availableHeight ) {
		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		p = javax.swing.SwingUtilities.convertPoint( asSeenBy.getAwtComponent(), p, layeredPane );

		this.dropProxy.setAvailableHeight( availableHeight );
		if( heightToAlignLeftCenterOn != null ) {
//			java.awt.Rectangle dropBounds = this.dropProxy.getBounds();
//			p.y += (heightToAlignLeftCenterOn - dropBounds.height) / 2;
			p.y += (heightToAlignLeftCenterOn - this.dropProxy.getAvailableHeight()) / 2;
			//p.x += 8;
		}

		
		//java.awt.event.MouseEvent mousePressedEvent = this.getMousePressedEvent();
		//p.x -= mousePressedEvent.getX();
		//p.y -= mousePressedEvent.getY();

		this.dropProxy.setLocation( p );
		if( this.dropProxy.getParent() != null ) {
			//pass
		} else {
			layeredPane.add( this.dropProxy, new Integer( 1 ) );
			layeredPane.setLayer( this.dropProxy, javax.swing.JLayeredPane.DEFAULT_LAYER );
		}
	}
	public void hideDropProxyIfNecessary() {
		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		if( this.dropProxy.getParent() != null ) {
			java.awt.Rectangle bounds = this.dropProxy.getBounds();
			if( layeredPane != null ) {
				layeredPane.remove( this.dropProxy );
				layeredPane.repaint( bounds );
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: hideDropProxyIfNecessary, layeredPane is null" );
			}
		}
	}
	public void handleCancel( java.util.EventObject e ) {
		Application.getSingleton().setDragInProgress( false );
		this.setActive( false );
		javax.swing.JLayeredPane layeredPane = getLayeredPane();
		if( layeredPane != null ) {
			java.awt.Rectangle bounds = this.dragProxy.getBounds();
			layeredPane.remove( this.dragProxy );
			layeredPane.repaint( bounds );
		}
		this.dragAndDropContext.handleCancel( e );
	}
}
