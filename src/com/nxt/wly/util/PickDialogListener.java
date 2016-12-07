package com.nxt.wly.util;

public interface PickDialogListener {
	public void onLeftBtnClick();
	public void onRightBtnClick();
	public void onListItemClick(int position, String string);
	public void onListItemLongClick(int position, String string);
	public void onCancel();
}
