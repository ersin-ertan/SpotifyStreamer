package com.nullcognition.spotifystreamer;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

public class Practice{

	Activity activity;


	public void sendDataToFragment(){
		Fragment fragment = activity.getFragmentManager().findFragmentById(R.id.fragment);
//		fragment.someMethod();
	}

	public void usingHandler(){
		Handler handler = new Handler(); // takes callback, looper
		Runnable r = new Runnable(){
			@Override
			public void run(){

			}
		};

		// Handler
		// The Handler class can schedule asynchronous callbacks at an absolute or relative time.
		// Handler objects also use the uptimeMillis() clock, and require an event loop
		// (normally present in any GUI application).

		handler.postDelayed(r, 100);
		handler.post(r);
		handler.postAtFrontOfQueue(r);
		handler.postAtTime(r, SystemClock.uptimeMillis() + 100);
		/*
		* uptimeMillis() is counted in milliseconds since the system was booted. This clock stops
		* when the system enters deep sleep (CPU off, display dark, device waiting for external
		* input), but is not affected by clock scaling, idle, or other power saving mechanisms.
		* This is the basis for most interval timing such as Thread.sleep(millls),
		* Object.wait(millis), and System.nanoTime(). This clock is guaranteed to be monotonic,
		 * and is suitable for interval timing when the interval does not span device sleep.
		 * Most methods that accept a timestamp value currently expect the uptimeMillis() clock.
		 * */

		handler.removeCallbacks(r); // does this imply that one runnable may post several different
		// callbacks?


		// MESSAGE
		// Defines a message containing a description and arbitrary data object that can be sent to a Handler.
		// implements Parcelable

		Parcel p = Parcel.obtain(); // Parcel's primitive types:
		// active objects, untyped containers, parcelables, bundles, primitives, primitive arrays


		// Container for a message (data and object references) that can be sent through an IBinder.
		// A Parcel can contain both flattened data that will be unflattened on the other side of the IPC
		// not general purpose but for High IPC
		Par par = Par.CREATOR.createFromParcel(p);
		Parcel out = Parcel.obtain();
		par.writeToParcel(out, Parcelable.PARCELABLE_WRITE_RETURN_VALUE); // read up on the flags
		par.someMeth();

		Message ms = Message.obtain(); // obtain to get message object from message pool(recycled)
		ms.arg1 = 1; // easy parameters that cover most use cases
		ms.arg2 = 2;
		ms.writeToParcel(out, Message.PARCELABLE_WRITE_RETURN_VALUE); // same method due to implements
		Runnable theCallback = ms.getCallback(); // when the handler receives the message
		ms.getWhen(); // targeted delivery time of method
		ms.getData(); // the bundle from associated event, lazy create
		// ms.obj; only framework parcelable objects to send, else use
		ms.setData(new Bundle()); // which can have serializable or parcelable, mostly for primitives
		ms.isAsynchronous(); // true if not subject to loader barrier synchronization
		ms.what = 1; // user defined code, under unique namespace meaning no collisions


		ms.setAsynchronous(true);
		/*
		 Certain operations, such as view invalidation, may introduce synchronization barriers into
		 the Looper's message queue to prevent subsequent messages from being delivered until some
		 condition is met. In the case of view invalidation, messages which are posted after a call
		 to invalidate() are suspended by means of a synchronization barrier until the next frame is
		 ready to be drawn. The synchronization barrier ensures that the invalidation request is
		 completely handled before resuming.

		 Asynchronous messages are exempt from synchronization barriers. They typically represent
		 interrupts, input events, and other signals that must be handled independently even while
		 other work has been suspended.
		 Note that asynchronous messages may be delivered out of order with respect to synchronous
		 messages although they are always delivered in order among themselves. If the relative
		 order of these messages matters then they probably should not be asynchronous
		 in the first place. Use with caution.
		 */


		handler.dispatchMessage(ms); // system messages
		int WHAT_CODE_DELETEABLE = 1;
		int WHAT_CODE_READABLE = 0;
		handler.hasMessages(WHAT_CODE_READABLE);
		handler.removeMessages(WHAT_CODE_DELETEABLE);
		// similar to deleting with runnables
		handler.sendEmptyMessage(WHAT_CODE_READABLE); // msg only with what code

		ms.recycle(); // frees the message, no longer usable

		Looper l = handler.getLooper();
		/*
		used to run a message loop for a thread. Threads by default do not have a message loop
		associated with them; to create one, call prepare() in the thread that is to run the loop,
		and then loop() to have it process messages until the loop is stopped.
		Most interaction with a message loop is through the Handler class.
		This is a typical example of the implementation of a Looper thread, using the separation of
		prepare() and loop() to create an initial Handler to communicate with the Looper.
*/
	}


}


class LooperThread extends Thread{
	public Handler mHandler;

	public void run(){
		Looper.prepare(); // init current thread as looper, allows handlers to reference the looper
		// before it is run, call loop, then quit when done

		mHandler = new Handler(){
			public void handleMessage(Message msg){
				// process incoming messages here
			}
		};

		Looper.loop();
	}
}

class Par implements Parcelable{
	private int mData;

	public int describeContents(){
		return 0;
	}

	public void someMeth(){}

	public void writeToParcel(Parcel out, int flags){
		out.writeInt(mData);
	}

	public static final Parcelable.Creator<Par> CREATOR
			= new Parcelable.Creator<Par>(){
		public Par createFromParcel(Parcel in){
			return new Par(in);
		}

		public Par[] newArray(int size){
			return new Par[size];
		}
	};

	private Par(Parcel in){
		mData = in.readInt();
	}
}
