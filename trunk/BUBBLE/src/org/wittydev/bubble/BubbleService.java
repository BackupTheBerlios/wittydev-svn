package org.wittydev.bubble;
import org.wittydev.config.ConfigEntry;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public interface BubbleService extends BubbleServiceListener{
	BubbleContext getRootContext();
	ConfigEntry getConfiguration();
	boolean isRunning();



}