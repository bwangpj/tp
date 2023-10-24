package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.contact.Contact;

public class DeleteCommand extends Command {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the contacts identified by the index numbers used in the displayed contact list.\n"
            + "Parameters: INDEX_1 INDEX_2 ... (must be positive integers)\n"
            + "Example: " + COMMAND_WORD + " 1 3 5";

    private final List<Index> targetIndices;

    public DeleteCommand(List<Index> targetIndices) {
        this.targetIndices = targetIndices;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Contact> lastShownList = model.getFilteredContactList();
        List<Contact> contactsToDelete = new ArrayList<>();

        // Collect contacts to delete
        for (Index index : targetIndices) {
            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException(Messages.INVALID_CONTACT_DISPLAYED_INDEX);
            }
            contactsToDelete.add(lastShownList.get(index.getZeroBased()));
        }

        // Delete the contacts
        for (Contact contact : contactsToDelete) {
            model.removeContact(contact);
        }

        // Format the deleted contacts for the message
        String formattedContacts = contactsToDelete.stream()
                .map(Contact::format)
                .collect(Collectors.joining(", "));

        return new CommandResult(String.format(Messages.DELETE_COMMAND_SUCCESS, formattedContacts));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof DeleteCommand)) {
            return false;
        }
        DeleteCommand otherDeleteCommand = (DeleteCommand) other;
        return targetIndices.equals(otherDeleteCommand.targetIndices);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndices", targetIndices)
                .toString();
    }
}
