package com.huntor.mscrm.app2.ui.component;

public interface OnSlideListener {
	
	public void onSlide(SlideState state);
	
	public static enum SlideState {
		BEGIN,ON,OFF
	};
}

 