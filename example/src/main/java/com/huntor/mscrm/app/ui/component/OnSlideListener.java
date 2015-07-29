package com.huntor.mscrm.app.ui.component;

public interface OnSlideListener {
	
	public void onSlide(SlideState state);
	
	public static enum SlideState {
		BEGIN,ON,OFF
	};
}

 