package ua.com.sipsoft.services.requests.arcive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ArchivedSheetFilter {

    /** The description. */
    @Builder.Default
    private String description = null;

    /** The sheet id. */
    @Builder.Default
    private Long sheetId = null;

    /**
     * To string.
     *
     * @return the string
     */
    @Override
    public String toString() {
	return String.format("ArchivedSheetFilter [description=\"%s\"]", description);
    }

}
