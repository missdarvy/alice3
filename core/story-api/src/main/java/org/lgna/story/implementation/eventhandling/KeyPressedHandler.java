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

package org.lgna.story.implementation.eventhandling;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lgna.common.ComponentExecutor;
import org.lgna.story.HeldKeyPolicy;
import org.lgna.story.Key;
import org.lgna.story.MultipleEventPolicy;
import org.lgna.story.event.ArrowKeyEvent;
import org.lgna.story.event.ArrowKeyPressListener;
import org.lgna.story.event.KeyEvent;
import org.lgna.story.event.KeyPressListener;
import org.lgna.story.event.NumberKeyEvent;
import org.lgna.story.event.NumberKeyPressListener;

import edu.cmu.cs.dennisc.java.util.Maps;

/**
 * @author Matt May
 */
public class KeyPressedHandler extends AbstractEventHandler<Object, KeyEvent> {

	private final Map<Object, CopyOnWriteArrayList<Object>> keyToListenersMap = new ConcurrentHashMap<Object, CopyOnWriteArrayList<Object>>();
	private final Object empty = new Object();
	private final Map<Object, HeldKeyPolicy> heldKeyMap = Maps.newConcurrentHashMap();
	private final Map<Object, Map<Key, Boolean>> firePolicyMap = Maps.newConcurrentHashMap();
	private long sleepTime = 33;

	public KeyPressedHandler() {
		keyToListenersMap.put( empty, new CopyOnWriteArrayList<Object>() );
	}

	private void internalAddListener( Object keyList, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy ) {
		heldKeyMap.put( keyList, heldKeyPolicy );
		firePolicyMap.put( keyList, new ConcurrentHashMap<Key, Boolean>() );
		if( validKeys == null ) {
			keyToListenersMap.get( empty ).add( keyList );
		} else {
			for( Key k : validKeys ) {
				if( keyToListenersMap.get( k ) == null ) {
					keyToListenersMap.put( k, new CopyOnWriteArrayList<Object>() );
				}
				keyToListenersMap.get( k ).add( keyList );
			}
		}
		registerIsFiringMap( keyList );
		registerPolicyMap( keyList, policy );
	}

	public void addListener( KeyPressListener keyList, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy ) {
		this.internalAddListener( keyList, policy, validKeys, heldKeyPolicy );
	}

	public void addListener( ArrowKeyPressListener keyList, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy ) {
		this.internalAddListener( keyList, policy, validKeys, heldKeyPolicy );
	}

	public void addListener( NumberKeyPressListener keyList, MultipleEventPolicy policy, List<Key> validKeys, HeldKeyPolicy heldKeyPolicy ) {
		this.internalAddListener( keyList, policy, validKeys, heldKeyPolicy );
	}

	public void fireAllTargeted( KeyEvent e ) {
		if( shouldFire ) {
			Key key = e.getKey();
			if( keyToListenersMap.get( key ) != null ) {
				for( Object listener : keyToListenersMap.get( key ) ) {
					fireEvent( listener, e );
				}
			}
			for( Object listener : keyToListenersMap.get( empty ) ) {
				fireEvent( listener, e );
			}
		}
	}

	@Override
	protected void fire( Object listener, KeyEvent event ) {
		if( listener instanceof ArrowKeyPressListener ) {
			ArrowKeyPressListener arrowListener = (ArrowKeyPressListener)listener;
			arrowListener.arrowKeyPressed( new ArrowKeyEvent( event ) );
		} else if( listener instanceof NumberKeyPressListener ) {
			NumberKeyPressListener numberListener = (NumberKeyPressListener)listener;
			numberListener.numberKeyPressed( new NumberKeyEvent( event ) );
		} else if( listener instanceof KeyPressListener ) {
			KeyPressListener keyListener = (KeyPressListener)listener;
			keyListener.keyPressed( event );
		}
	}

	public void handleKeyPress( final KeyEvent event ) {
		if( shouldFire ) {
			final Key key = event.getKey();
			for( Object listener : listenerMapFor( key )) {
				firePressEvent( event, key, listener );
			}
		}
	}

	public void handleKeyRelease( KeyEvent event ) {
		if( shouldFire ) {
			Key key = event.getKey();
			for( Object listener : listenerMapFor( key )) {
				fireReleaseEvent( event, key, listener );
			}
		}
	}

	private CopyOnWriteArrayList<Object> listenerMapFor( Key key ) {
		CopyOnWriteArrayList<Object> listenerMap = keyToListenersMap.get( key );
		if( listenerMap == null ) {
			listenerMap = keyToListenersMap.get( empty );
		}
		return listenerMap;
	}

	private void firePressEvent( KeyEvent event, Key key, Object listener ) {
		if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_PRESS ) {
			if(isFirstPress( key, listener )) {
				System.out.println( "FIRE_ONCE_ON_PRESS" );
				fireEvent( listener, event );
			}
		} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_RELEASE ) {
			//pass
		} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_MULTIPLE ) {
			if(isFirstPress( key, listener )) {
				final ComponentExecutor thread = new ComponentExecutor( new Runnable() {
					@Override
					public void run() {
						while( firePolicyMap.get( listener ).get( key ) ) {
							fireEvent( listener, event );
							try {
								Thread.sleep( sleepTime );
							} catch( InterruptedException e ) {
								e.printStackTrace();
							}
						}
					}
				}, "keyPressedThread" );
				thread.start();
			}
		}
	}

	private boolean isFirstPress( Key key, Object listener ) {
		boolean firstPress = !isKeyPressed(key, listener);
		if (firstPress) {
			firePolicyMap.get( listener ).put( key, true );
		}
		return firstPress;
	}

	private boolean isKeyPressed( Key key, Object listener ) {
		// This entry should only be null if the key was pressed while the window didn't have focus and was released after it gained focus.
		return ( firePolicyMap.get( listener ).get( key ) != null ) && firePolicyMap.get( listener ).get( key );
	}

	private void fireReleaseEvent( KeyEvent event, Key key, Object listener ) {
		if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_PRESS ) {
			releaseKey( key, listener );
		} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_ONCE_ON_RELEASE ) {
			fireEvent( listener, event );
		} else if( heldKeyMap.get( listener ) == HeldKeyPolicy.FIRE_MULTIPLE ) {
			firePolicyMap.get( listener ).put( key, false );
		}
	}

	private void releaseKey( Key key, Object listener ) {
		if(isKeyPressed( key, listener )) {
			firePolicyMap.get( listener ).put( key, false );
		}
	}

	void releaseAllKeys() {
		for ( Map<Key, Boolean> keyMap : firePolicyMap.values() ) {
			for ( Key key : keyMap.keySet() ) {
				keyMap.put( key, false );
			}
		}
	}
}
