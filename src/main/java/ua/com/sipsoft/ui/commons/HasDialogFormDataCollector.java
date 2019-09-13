package ua.com.sipsoft.ui.commons;

/**
 * The Interface HasDialogFormDataCollector.
 */
public interface HasDialogFormDataCollector {

	/**
	 * Distribute operation data.
	 *
	 * @param operationData the operation data
	 */
	void distributeOperationData(Object operationData);

	/**
	 * Collect operation data.
	 *
	 * @return the object
	 */
	Object collectOperationData();

}
