package org.wittydev.bubble;

import org.wittydev.core.WDException;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2002
 * Company:
 * @author
 * @version 1.0
 */

public interface BubbleServiceListener {
	public void startService(BubbleServiceEvent event) throws WDException;
	public void stopService();
}