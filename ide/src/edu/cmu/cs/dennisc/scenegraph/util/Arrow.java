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
package edu.cmu.cs.dennisc.scenegraph.util;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.scenegraph.*;
import edu.cmu.cs.dennisc.scenegraph.Cylinder.BottomToTopAxis;

/**
 * @author Dennis Cosgrove
 */
public class Arrow extends Transformable {
	
	private Visual sgVisualCylinder;
	private Visual sgVisualCone;
	private Cylinder sgCone;
	private Cylinder sgCylinder;
	private Transformable sgTransformableCone;
	private BottomToTopAxis bottomToTopAxis;
	
	public Arrow( double lengthCylinder, double radiusCylinder, double lengthCone, double radiusCone, Cylinder.BottomToTopAxis bottomToTopAxis, SingleAppearance frontFacingAppearance, boolean isBottomCapDesired ) {
		this.bottomToTopAxis = bottomToTopAxis;
		
		this.sgVisualCylinder = new Visual();
		sgVisualCylinder.frontFacingAppearance.setValue( frontFacingAppearance );
		
		this.sgCylinder = new Cylinder();
		sgCylinder.topRadius.setValue( radiusCylinder );
		sgCylinder.bottomRadius.setValue( radiusCylinder );
		sgCylinder.length.setValue( lengthCylinder );
		sgCylinder.bottomToTopAxis.setValue( this.bottomToTopAxis );
		sgCylinder.hasTopCap.setValue( false );
		//todo?
		sgCylinder.hasBottomCap.setValue( isBottomCapDesired );
		
		this.sgTransformableCone = new Transformable();		
		setConeTranslation(lengthCylinder);

		this.sgVisualCone = new Visual();
		sgVisualCone.frontFacingAppearance.setValue( frontFacingAppearance );

		this.sgCone = new Cylinder();
	    sgCone.topRadius.setValue( 0.0 );
	    sgCone.bottomRadius.setValue( radiusCone );
	    sgCone.length.setValue( lengthCone );
	    sgCone.bottomToTopAxis.setValue( this.bottomToTopAxis );
	    sgCone.hasTopCap.setValue( false ); //redundant
	    sgCone.hasBottomCap.setValue( true );
	    
	    
	    sgVisualCylinder.geometries.setValue( new Geometry[] { sgCylinder } );
	    sgVisualCone.geometries.setValue( new Geometry[] { sgCone } );
	    
	    sgVisualCylinder.setParent( this );
	    sgTransformableCone.setParent( this );
	    sgVisualCone.setParent( sgTransformableCone );
	}
	
	private void setConeTranslation(double lengthCylinder)
	{
		edu.cmu.cs.dennisc.math.Vector3 translation = edu.cmu.cs.dennisc.math.Vector3.createMultiplication(  
				new edu.cmu.cs.dennisc.math.Vector3( lengthCylinder, lengthCylinder, lengthCylinder ), 
				this.bottomToTopAxis.accessVector() 
			);
			AffineMatrix4x4 currentTransform = this.sgTransformableCone.localTransformation.getValue();
			currentTransform.translation.set(translation);
			this.sgTransformableCone.localTransformation.setValue(currentTransform);
	}
	
	public void resize( double lengthCylinder, double radiusCylinder, double lengthCone, double radiusCone )
	{
		this.sgCylinder.topRadius.setValue( radiusCylinder );
		this.sgCylinder.bottomRadius.setValue( radiusCylinder );
		this.sgCylinder.length.setValue( lengthCylinder );
		this.sgCone.topRadius.setValue( 0.0 );
	    this.sgCone.bottomRadius.setValue( radiusCone );
	    this.sgCone.length.setValue( lengthCone );
	    setConeTranslation(lengthCylinder);
		
	}
	
	public void setFrontFacingAppearance( SingleAppearance frontFacingAppearance )
	{
		this.sgVisualCone.frontFacingAppearance.setValue(frontFacingAppearance);
		this.sgVisualCylinder.frontFacingAppearance.setValue(frontFacingAppearance);
	}
	
	public void setVisualShowing(boolean isShowing)
	{
		this.sgVisualCone.isShowing.setValue(isShowing);
		this.sgVisualCylinder.isShowing.setValue(isShowing);
	}
}
