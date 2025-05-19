/*
 * © 2024 SafeGuard
 * All rights reserved.
 *
 * Author: Mahesh
 *
 * Custom exception for handling common application errors.
 */

package org.safeguard.insurance.exception;

@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {

	private final String errorMessage;

	public ApplicationException(String errorMessage) {
		super(errorMessage);
		this.errorMessage = errorMessage;
	}

	@Override
	public String getMessage() {
		return errorMessage;
	}
}
