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

/**
 * @author Dennis Cosgrove
 */
public abstract class DialogOperation extends Operation<DialogOperationContext> {
	public DialogOperation(Group group, java.util.UUID individualUUID) {
		super(group, individualUUID);
	}
	@Override
	protected DialogOperationContext createContext( ModelContext< ? > parent, java.util.EventObject e, ViewController< ?, ? > viewController ) {
		return parent.createDialogOperationContext( this, e, viewController );
	}
	protected java.awt.Point getDesiredDialogLocation( Dialog dialog ) {
		return null;
	}
	protected java.awt.Dimension getDesiredDialogSize( Dialog dialog ) {
		return null;
	}
	
	private Dialog activeDialog;
	public Dialog getActiveDialog() {
		return this.activeDialog;
	}
	
	protected abstract Container<?> createContentPane(DialogOperationContext context, Dialog dialog);
	protected abstract void releaseContentPane(DialogOperationContext context, Dialog dialog, Container<?> contentPane );
	@Override
	protected final void perform( final DialogOperationContext context ) {
		ViewController<?,?> viewController = context.getViewController();
		Component<?> owner;
		if( viewController != null ) {
			owner = viewController;
		} else {
			owner = Application.getSingleton().getFrame().getContentPanel();
		}
		final Dialog dialog = new Dialog( owner );
		dialog.setTitle( this.getName() );
		dialog.setDefaultCloseOperation( edu.cmu.cs.dennisc.croquet.Dialog.DefaultCloseOperation.DO_NOTHING );

		Container<?> contentPane = this.createContentPane(context, dialog);
		assert contentPane != null;
		dialog.getAwtWindow().setContentPane( contentPane.getAwtComponent() );

		java.awt.event.WindowListener windowListener = new java.awt.event.WindowListener() {
			public void windowOpened( java.awt.event.WindowEvent e ) {
				context.handleWindowOpened( e );
			}
			public void windowClosing( java.awt.event.WindowEvent e ) {
				dialog.setVisible( false );
				context.handleWindowClosed( e );
			}
			public void windowClosed( java.awt.event.WindowEvent e ) {
			}
			public void windowActivated( java.awt.event.WindowEvent e ) {
			}
			public void windowDeactivated( java.awt.event.WindowEvent e ) {
			}
			public void windowDeiconified( java.awt.event.WindowEvent e ) {
			}
			public void windowIconified( java.awt.event.WindowEvent e ) {
			}
		};
		dialog.addWindowListener( windowListener );
		java.awt.Dimension size = this.getDesiredDialogSize( dialog );
		if( size != null ) {
			dialog.getAwtWindow().setSize( size );
		} else {
			dialog.pack();
		}
		java.awt.Point location = this.getDesiredDialogLocation( dialog );
		if( location != null ) {
			dialog.setLocation( location );
		} else {
			edu.cmu.cs.dennisc.java.awt.WindowUtilties.setLocationOnScreenToCenteredWithin( dialog.getAwtWindow(), Application.getSingleton().getFrame().getAwtWindow() ); 
		}
		
		this.activeDialog = dialog;
		try {
			dialog.setVisible( true );
			this.releaseContentPane( context, dialog, contentPane );
			dialog.removeWindowListener( windowListener );
			context.finish();
		} finally {
			this.activeDialog = null;
		}
	}
}
