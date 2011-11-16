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

package edu.cmu.cs.dennisc.lookingglass.opengl;

import static javax.media.opengl.GL.*;

/**
 * @author Dennis Cosgrove
 */
public class Picker implements edu.cmu.cs.dennisc.lookingglass.Picker {
	private static final int SELECTION_CAPACITY = 256;
	private final AbstractLookingGlass lookingGlass;
	private final PickContext pickContext = new PickContext();
	private final java.nio.IntBuffer selectionAsIntBuffer;
	private PickParameters pickParameters = null;
	private javax.media.opengl.GLPbuffer drawable;
	
	private javax.media.opengl.GLEventListener glEventListener = new javax.media.opengl.GLEventListener() {
		public void init( javax.media.opengl.GLAutoDrawable drawable ) {
		}
		public void reshape( javax.media.opengl.GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4 ) {
		}
		public void display( javax.media.opengl.GLAutoDrawable drawable ) {
			Picker.this.performPick( drawable.getGL() );
		}
		public void displayChanged( javax.media.opengl.GLAutoDrawable arg0, boolean arg1, boolean arg2 ) {
		}
//		public void dispose( javax.media.opengl.GLAutoDrawable drawable ) {
//		}
	};
	
	public Picker( AbstractLookingGlass lookingGlass ) {
		this.lookingGlass = lookingGlass;
		final int SIZEOF_INT = 4;
		java.nio.ByteBuffer byteBuffer = java.nio.ByteBuffer.allocateDirect( SIZEOF_INT * SELECTION_CAPACITY );
		byteBuffer.order( java.nio.ByteOrder.nativeOrder() );
		this.selectionAsIntBuffer = byteBuffer.asIntBuffer();
	}

	private javax.media.opengl.GLPbuffer getUpToDateBuffer() {
		java.awt.Dimension lgSize = this.lookingGlass.getSize();
		if( lgSize.width > 0 && lgSize.height > 0 ) {
			javax.media.opengl.GLAutoDrawable shareDrawable = this.lookingGlass.getGLAutoDrawable(); 
			if( shareDrawable != null ) {
				javax.media.opengl.GLContext shareContext;
				try {
					shareContext = shareDrawable.getContext();
				} catch( NullPointerException npe ) {
					shareContext = null;
					Thread.dumpStack();
				}
				if (this.drawable != null) {
					if (lgSize.width != this.drawable.getWidth() || lgSize.height != this.drawable.getHeight()) {
						this.release();
					}
				}
				if( shareContext != null ) {
					if( this.drawable != null ) {
						//pass
					} else {
						this.drawable = LookingGlassFactory.getSingleton().createGLPbuffer( lgSize.width, lgSize.height, shareContext );
						this.drawable.addGLEventListener( this.glEventListener );
					}
				}
			}
		}
		return this.drawable;
	}
	/*package-protected*/ void release() {
		if (this.drawable != null) {
			this.drawable.removeGLEventListener( this.glEventListener );
			this.drawable.destroy();
			this.drawable = null;
		}
	}

	private static ConformanceTestResults conformanceTestResults = null;
	private void performPick( javax.media.opengl.GL gl ) {
		this.pickContext.gl = gl;
		if( conformanceTestResults != null ) {
			//pass
		} else {
			conformanceTestResults = new ConformanceTestResults( this.pickContext.gl );
			if( conformanceTestResults.isPickFunctioningCorrectly() ) {
				//pass
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl isPickFunctioningCorrectly:", conformanceTestResults.isPickFunctioningCorrectly() );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl version:", conformanceTestResults.getVersion() );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl vendor:", conformanceTestResults.getVendor() );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl renderer:", conformanceTestResults.getRenderer() );
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "opengl extensions:", conformanceTestResults.getExtensions() );
			}
		}
		
		edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver = this.pickParameters.getPickObserver();
		if( pickObserver != null ) {
			pickObserver.prePick();
			ChangeHandler.handleBufferedChanges();
		}
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = this.pickParameters.getSGCamera();
		AbstractCameraAdapter< ? extends edu.cmu.cs.dennisc.scenegraph.AbstractCamera > cameraAdapter = AdapterFactory.getAdapterFor( sgCamera );

		this.selectionAsIntBuffer.rewind();
		this.pickContext.gl.glSelectBuffer( SELECTION_CAPACITY, this.selectionAsIntBuffer );

		this.pickContext.gl.glRenderMode( GL_SELECT );
		this.pickContext.gl.glInitNames();

		java.awt.Rectangle actualViewport = this.lookingGlass.getActualViewport( sgCamera );
		this.pickContext.gl.glViewport( actualViewport.x, actualViewport.y, actualViewport.width, actualViewport.height );
		cameraAdapter.performPick( this.pickContext, this.pickParameters, actualViewport, conformanceTestResults );
		this.pickContext.gl.glFlush();

		this.selectionAsIntBuffer.rewind();
		int length = this.pickContext.gl.glRenderMode( GL_RENDER );

		//todo: invesigate negative length
		//assert length >= 0;

		if( length > 0 ) {
			SelectionBufferInfo[] selectionBufferInfos = new SelectionBufferInfo[ length ];
			int offset = 0;
			for( int i = 0; i < length; i++ ) {
				selectionBufferInfos[ i ] = new SelectionBufferInfo( this.pickContext, this.selectionAsIntBuffer, offset );
				offset += 7;
			}

			if( conformanceTestResults.isPickFunctioningCorrectly() ) {
				double x = this.pickParameters.getX();
				double y = this.pickParameters.getFlippedY( actualViewport );

				edu.cmu.cs.dennisc.math.Matrix4x4 m = new edu.cmu.cs.dennisc.math.Matrix4x4();
				m.translation.set( actualViewport.width - 2*(x-actualViewport.x), actualViewport.height - 2*(y-actualViewport.y), 0, 1 );
				edu.cmu.cs.dennisc.math.ScaleUtilities.applyScale( m, actualViewport.width, actualViewport.height, 1.0 );

				edu.cmu.cs.dennisc.math.Matrix4x4 p = new edu.cmu.cs.dennisc.math.Matrix4x4();
				cameraAdapter.getActualProjectionMatrix( p, actualViewport );
				
				m.applyMultiplication( p );
				m.invert();
				for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
					selectionBufferInfo.updatePointInSource( m );
				}
			} else {
				edu.cmu.cs.dennisc.math.Ray ray = new edu.cmu.cs.dennisc.math.Ray();
				ray.setNaN();
				cameraAdapter.getRayAtPixel( ray, pickParameters.getX(), pickParameters.getY(), actualViewport);
				ray.accessDirection().normalize();
				edu.cmu.cs.dennisc.math.AffineMatrix4x4 inverseAbsoluteTransformation = sgCamera.getInverseAbsoluteTransformation();
				for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
					selectionBufferInfo.updatePointInSource( ray, inverseAbsoluteTransformation );
				}
			}

			if( length > 1 ) {
//				float front0 = selectionBufferInfos[ 0 ].getZFront();
//				boolean isDifferentiated = false;
//				for( int i=1; i<length; i++ ) {
//					if( front0 == selectionBufferInfos[ i ].getZFront() ) {
//						//pass
//					} else {
//						isDifferentiated = true;
//						break;
//					}
//				}
//				java.util.Comparator< SelectionBufferInfo > comparator;
//				if( isDifferentiated ) {
//					comparator = new java.util.Comparator< SelectionBufferInfo >() {
//						public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
//							return Float.compare( sbi1.getZFront(), sbi2.getZFront() );
//						}
//					};
//				} else {
//					if( conformanceTestResults.isPickFunctioningCorrectly() ) {
//						edu.cmu.cs.dennisc.print.PrintUtilities.println( "todo: conformance test reports pick is functioning correctly" );
//						comparator = null;
//					} else { 
//						edu.cmu.cs.dennisc.math.Ray ray = new edu.cmu.cs.dennisc.math.Ray();
//						ray.setNaN();
//						cameraAdapter.getRayAtPixel( ray, pickParameters.getX(), pickParameters.getY(), actualViewport);
//						for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
//							selectionBufferInfo.updatePointInSource( ray );
//						}
//						comparator = new java.util.Comparator< SelectionBufferInfo >() {
//							public int compare( SelectionBufferInfo sbi1, SelectionBufferInfo sbi2 ) {
//								return Double.compare( sbi1.getPointInSource().z, sbi2.getPointInSource().z );
//							}
//						};
//					}
//				}
				java.util.Comparator< SelectionBufferInfo > comparator;
				if( conformanceTestResults.isPickFunctioningCorrectly() ) {
					comparator = new java.util.Comparator<SelectionBufferInfo>() {
						public int compare(SelectionBufferInfo sbi1, SelectionBufferInfo sbi2) {
							return Float.compare(sbi1.getZFront(), sbi2.getZFront());
						}
					};
				} else {
					comparator = new java.util.Comparator<SelectionBufferInfo>() {
						public int compare(SelectionBufferInfo sbi1, SelectionBufferInfo sbi2) {
							double z1 = -sbi1.getPointInSource().z;
							double z2 = -sbi2.getPointInSource().z;
							return Double.compare(z1, z2);
						}
					};
				}
				java.util.Arrays.sort( selectionBufferInfos, comparator );
			}
			for( SelectionBufferInfo selectionBufferInfo : selectionBufferInfos ) {
				this.pickParameters.addPickResult( sgCamera, selectionBufferInfo.getSGVisual(), selectionBufferInfo.isFrontFacing(), selectionBufferInfo.getSGGeometry(), selectionBufferInfo.getSubElement(), selectionBufferInfo.getPointInSource() );
			}
		}
		if( pickObserver != null ) {
			pickObserver.postPick();
			ChangeHandler.handleBufferedChanges();
		}

	}
	
	private edu.cmu.cs.dennisc.lookingglass.PickResult pickFrontMost( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		javax.media.opengl.GLPbuffer pBuffer = getUpToDateBuffer();
		this.pickParameters = new PickParameters( pBuffer, sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
		try {
			if( sgCamera != null ) {
//				if( pBuffer.isRealized() ) {
					pBuffer.display();
//				} else {
//					Thread.dumpStack();
//				}
			}
			return this.pickParameters.accessFrontMostPickResult();
		} finally {
			this.pickParameters = null;
		}
	}
	public java.util.List< edu.cmu.cs.dennisc.lookingglass.PickResult > pickAll( edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera, int xPixel, int yPixel, boolean isSubElementRequired, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		javax.media.opengl.GLPbuffer pBuffer = getUpToDateBuffer();
		this.pickParameters = new PickParameters( pBuffer, sgCamera, xPixel, yPixel, isSubElementRequired, pickObserver );
		try {
			if( sgCamera != null ) {
//				if( pBuffer.isRealized() ) {
					pBuffer.display();
//				} else {
//					Thread.dumpStack();
//				}
			}
			return this.pickParameters.accessAllPickResults();
		} finally {
			this.pickParameters = null;
		}
	}
	
	public java.util.List< edu.cmu.cs.dennisc.lookingglass.PickResult > pickAll( int xPixel, int yPixel, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy pickSubElementPolicy ) {
		return this.pickAll( xPixel, yPixel, pickSubElementPolicy, null );
	}
	public java.util.List< edu.cmu.cs.dennisc.lookingglass.PickResult > pickAll( int xPixel, int yPixel, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy pickSubElementPolicy, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		//return this.lookingGlass.pickAll( xPixel, yPixel, pickSubElementPolicy, pickObserver );
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = this.lookingGlass.getCameraAtPixel( xPixel, yPixel );
		return this.pickAll( sgCamera, xPixel, yPixel, pickSubElementPolicy==edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy.REQUIRED, pickObserver );
	}
	public edu.cmu.cs.dennisc.lookingglass.PickResult pickFrontMost(int xPixel, int yPixel, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy pickSubElementPolicy ) {
		return this.pickFrontMost( xPixel, yPixel, pickSubElementPolicy, null );
	}
	public edu.cmu.cs.dennisc.lookingglass.PickResult pickFrontMost( int xPixel, int yPixel, edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy pickSubElementPolicy, edu.cmu.cs.dennisc.lookingglass.PickObserver pickObserver ) {
		//return this.lookingGlass.pickFrontMost( xPixel, yPixel, pickSubElementPolicy, pickObserver );
		edu.cmu.cs.dennisc.scenegraph.AbstractCamera sgCamera = this.lookingGlass.getCameraAtPixel( xPixel, yPixel );
		return this.pickFrontMost( sgCamera, xPixel, yPixel, pickSubElementPolicy==edu.cmu.cs.dennisc.lookingglass.PickSubElementPolicy.REQUIRED, pickObserver );
	}
}
