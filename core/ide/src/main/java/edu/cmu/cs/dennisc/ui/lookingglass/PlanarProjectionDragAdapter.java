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
package edu.cmu.cs.dennisc.ui.lookingglass;

import edu.cmu.cs.dennisc.java.awt.event.MouseEventUtilities;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Plane;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Ray;
import edu.cmu.cs.dennisc.scenegraph.AbstractCamera;
import edu.cmu.cs.dennisc.ui.DragStyle;

import java.awt.Point;
import java.awt.event.MouseEvent;

/**
 * @author Dennis Cosgrove
 */
public abstract class PlanarProjectionDragAdapter extends OnscreenLookingGlassDragAdapter {
  protected abstract Plane getPlaneInAbsolute();

  protected abstract void handlePointInAbsolute(Point3 xyzInAbsolute);

  @Override
  protected boolean isAcceptable(MouseEvent e) {
    return MouseEventUtilities.isQuoteLeftUnquoteMouseButton(e);
  }

  private Point3 getPointInAbsolutePlane(Point p) {
    Point3 rv;
    AbstractCamera sgCamera = getOnscreenRenderTarget().getCameraAtPixel(p.x, p.y);
    if (sgCamera != null) {
      Ray ray = getOnscreenRenderTarget().getRayAtPixel(p.x, p.y, sgCamera);
      AffineMatrix4x4 m = sgCamera.getAbsoluteTransformation();
      ray.transform(m);
      Plane planeInAbsolute = this.getPlaneInAbsolute();
      double t = planeInAbsolute.intersect(ray);
      rv = ray.getPointAlong(t);
    } else {
      rv = null;
    }
    return rv;
  }

  @Override
  protected void handleMousePress(Point current, DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange) {
    if (isOriginalAsOpposedToStyleChange) {
    } else {
    }
  }

  @Override
  protected void handleMouseDrag(Point current, int xDeltaSince0, int yDeltaSince0, int xDeltaSincePrevious, int yDeltaSincePrevious, DragStyle dragStyle) {
    Point3 xyzInAbsolute = getPointInAbsolutePlane(current);
    if (xyzInAbsolute != null) {
      handlePointInAbsolute(xyzInAbsolute);
    }
  }

  @Override
  protected Point handleMouseRelease(Point rv, DragStyle dragStyle, boolean isOriginalAsOpposedToStyleChange) {
    return rv;
  }
}
