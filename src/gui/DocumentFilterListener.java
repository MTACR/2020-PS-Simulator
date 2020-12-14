package gui;

import javax.swing.text.DocumentFilter;

public interface DocumentFilterListener {

    void documentFilterValidationFailed(DocumentFilter filter, String message);
}
