package ca.uhn.fhir.jpa.util;

import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

/*
 * #%L
 * HAPI FHIR JPA Server
 * %%
 * Copyright (C) 2014 - 2017 University Health Network
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

public class StopWatch {

	private long myStarted = System.currentTimeMillis();

	/**
	 * Constructor
	 */
	public StopWatch() {
		super();
	}

	/**
	 * Constructor
	 */
	public StopWatch(Date theNow) {
		myStarted = theNow.getTime();
	}

	public long getMillisAndRestart() {
		long now = System.currentTimeMillis();
		long retVal = now - myStarted;
		myStarted = now;
		return retVal;
	}

	public long getMillis() {
		long now = System.currentTimeMillis();
		long retVal = now - myStarted;
		return retVal;
	}

	public long getMillis(Date theNow) {
		long retVal = theNow.getTime() - myStarted;
		return retVal;
	}

	public Date getStartedDate() {
		return new Date(myStarted);
	}

	/**
	 * Formats value in the format [DD d ]HH:mm:ss.SSSS
	 */
	@Override
	public String toString() {
		return formatMillis(getMillis());
	}

	static public String formatMillis(long val) {
		StringBuilder buf = new StringBuilder(20);
		if (val >= DateUtils.MILLIS_PER_DAY) {
			long days = val / DateUtils.MILLIS_PER_DAY;
			append(buf, "", 1, days);
			if (days > 1) {
				buf.append(" days ");
			} else if (days == 1) {
				buf.append(" day ");
			}
			append(buf, "", 2, ((val % DateUtils.MILLIS_PER_DAY) / DateUtils.MILLIS_PER_HOUR));
		} else {
			append(buf, "", 2, ((val % DateUtils.MILLIS_PER_DAY) / DateUtils.MILLIS_PER_HOUR));
		}
		append(buf, ":", 2, ((val % DateUtils.MILLIS_PER_HOUR) / DateUtils.MILLIS_PER_MINUTE));
		append(buf, ":", 2, ((val % DateUtils.MILLIS_PER_MINUTE) / DateUtils.MILLIS_PER_SECOND));
		append(buf, ".", 3, (val % DateUtils.MILLIS_PER_SECOND));
		return buf.toString();
	}

	/** Append a right-aligned and zero-padded numeric value to a `StringBuilder`. */
	static private void append(StringBuilder tgt, String pfx, int dgt, long val) {
		tgt.append(pfx);
		if (dgt > 1) {
			int pad = (dgt - 1);
			for (long xa = val; xa > 9 && pad > 0; xa /= 10) {
				pad--;
			}
			for (int xa = 0; xa < pad; xa++) {
				tgt.append('0');
			}
		}
		tgt.append(val);
	}

	public int getMillisPerOperation(int theNumOperations) {
		return (int)(((double) getMillis()) / Math.max(1.0, theNumOperations));
	}

}
