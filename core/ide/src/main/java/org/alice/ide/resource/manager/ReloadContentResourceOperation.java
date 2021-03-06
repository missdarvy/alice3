/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.alice.ide.resource.manager;

import org.alice.ide.ast.importers.AudioResourceImporter;
import org.alice.ide.ast.importers.ImageResourceImporter;
import org.lgna.common.Resource;
import org.lgna.common.resources.AudioResource;
import org.lgna.common.resources.ImageResource;
import org.lgna.croquet.Application;
import org.lgna.croquet.ItemState;
import org.lgna.croquet.edits.AbstractEdit;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.UserActivity;
import org.lgna.croquet.views.Frame;

import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
/*package-private*/final class ReloadContentResourceOperation extends SelectedResourceOperation {
  private abstract static class Capsule<E extends Resource> {
    private String originalFileName;
    //private String name;
    private String contentType;
    private byte[] data;

    public Capsule(E resource) {
      this.originalFileName = resource.getOriginalFileName();
      //this.name = resource.getName();
      this.contentType = resource.getContentType();
      this.data = resource.getData();
    }

    public E update(E rv) {
      rv.setOriginalFileName(this.originalFileName);
      //rv.setName( this.name );
      rv.setContent(this.contentType, this.data);
      return rv;
    }
  }

  private static class ImageCapsule extends Capsule<ImageResource> {
    private int width;
    private int height;

    public ImageCapsule(ImageResource imageResource) {
      super(imageResource);
      this.width = imageResource.getWidth();
      this.height = imageResource.getHeight();
    }

    @Override
    public ImageResource update(ImageResource rv) {
      rv = super.update(rv);
      rv.setWidth(this.width);
      rv.setHeight(this.height);
      return rv;
    }
  }

  private static class AudioCapsule extends Capsule<AudioResource> {
    private double duration;

    public AudioCapsule(AudioResource audioResource) {
      super(audioResource);
      this.duration = audioResource.getDuration();
    }

    @Override
    public AudioResource update(AudioResource rv) {
      rv = super.update(rv);
      rv.setDuration(this.duration);
      return rv;
    }
  }

  public ReloadContentResourceOperation(ItemState<Resource> resourceState) {
    super(UUID.fromString("05f5ede7-194a-45b2-bb97-c3d23aedf5b9"), resourceState);
  }

  @Override
  protected Edit createEdit(UserActivity userActivity, final Resource resource) {
    if (resource != null) {
      final Capsule prevCapsule;
      final Capsule nextCapsule;
      Frame frame = Application.getActiveInstance().getDocumentFrame().getFrame();
      if (resource instanceof ImageResource) {
        ImageResource prevImageResource = (ImageResource) resource;
        ImageResource nextImageResource = ImageResourceImporter.getInstance().createValue("Replace Image");
        if (nextImageResource != null) {
          prevCapsule = new ImageCapsule(prevImageResource);
          nextCapsule = new ImageCapsule(nextImageResource);
        } else {
          prevCapsule = null;
          nextCapsule = null;
        }
      } else if (resource instanceof AudioResource) {
        AudioResource prevAudioResource = (AudioResource) resource;
        AudioResource nextAudioResource = AudioResourceImporter.getInstance().createValue("Replace Audio");
        if (nextAudioResource != null) {
          prevCapsule = new AudioCapsule(prevAudioResource);
          nextCapsule = new AudioCapsule(nextAudioResource);
        } else {
          prevCapsule = null;
          nextCapsule = null;
        }
      } else {
        prevCapsule = null;
        nextCapsule = null;
      }
      if ((prevCapsule != null) && (nextCapsule != null)) {
        return new AbstractEdit(userActivity) {
          @Override
          protected final void doOrRedoInternal(boolean isDo) {
            nextCapsule.update(resource);
          }

          @Override
          protected final void undoInternal() {
            prevCapsule.update(resource);
          }

          @Override
          protected void appendDescription(StringBuilder rv, DescriptionStyle descriptionStyle) {
            rv.append("reload content");
          }
        };
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
}
