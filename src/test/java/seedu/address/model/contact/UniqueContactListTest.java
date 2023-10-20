package seedu.address.model.contact;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TestData.ALICE;
import static seedu.address.testutil.TestData.VALID_NOTE_BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.contact.exceptions.ContactNotFoundException;
import seedu.address.model.contact.exceptions.DuplicateContactException;
import seedu.address.testutil.ContactBuilder;
import seedu.address.testutil.TestData;



public class UniqueContactListTest {
    private final UniqueContactList uniqueContactList = new UniqueContactList();

    @Test
    public void contains_contactNotInList_returnsFalse() {
        assertFalse(uniqueContactList.contains(ALICE));
    }

    @Test
    public void contains_contactInList_returnsTrue() {
        uniqueContactList.add(ALICE);
        assertTrue(uniqueContactList.contains(ALICE));
    }

    @Test
    public void contains_contactWithSameIdentityFieldsInList_returnsTrue() {
        uniqueContactList.add(ALICE);
        Contact editedAlice = new ContactBuilder(ALICE)
                .withNote(VALID_NOTE_BOB)
                .withTags(TestData.Valid.Tag.ALPHANUMERIC_SPACES)
                .build();
        assertTrue(uniqueContactList.contains(editedAlice));
    }

    @Test
    public void add_duplicateContact_throwsDuplicateContactException() {
        uniqueContactList.add(ALICE);
        assertThrows(DuplicateContactException.class, () -> uniqueContactList.add(ALICE));
    }

    @Test
    public void setContact_targetContactNotInList_throwsContactNotFoundException() {
        assertThrows(ContactNotFoundException.class, () -> uniqueContactList.setContact(ALICE, ALICE));
    }

    @Test
    public void setContact_editedContactIsSameContact_success() {
        uniqueContactList.add(ALICE);
        uniqueContactList.setContact(ALICE, ALICE);
        UniqueContactList expectedUniqueContactList = new UniqueContactList();
        expectedUniqueContactList.add(ALICE);
        assertEquals(expectedUniqueContactList, uniqueContactList);
    }

    @Test
    public void setContact_editedContactHasSameIdentity_success() {
        uniqueContactList.add(ALICE);
        Contact editedAlice = new ContactBuilder(ALICE)
                .withNote(VALID_NOTE_BOB)
                .withTags(TestData.Valid.Tag.ALPHANUMERIC_SPACES)
                .build();
        uniqueContactList.setContact(ALICE, editedAlice);
        UniqueContactList expectedUniqueContactList = new UniqueContactList();
        expectedUniqueContactList.add(editedAlice);
        assertEquals(expectedUniqueContactList, uniqueContactList);
    }

    @Test
    public void setContact_editedContactHasDifferentIdentity_success() {
        uniqueContactList.add(ALICE);
        uniqueContactList.setContact(ALICE, TestData.Valid.Contact.BOB);
        UniqueContactList expectedUniqueContactList = new UniqueContactList();
        expectedUniqueContactList.add(TestData.Valid.Contact.BOB);
        assertEquals(expectedUniqueContactList, uniqueContactList);
    }

    @Test
    public void setContact_editedContactHasNonUniqueIdentity_throwsDuplicateContactException() {
        uniqueContactList.add(ALICE);
        uniqueContactList.add(TestData.Valid.Contact.BOB);
        assertThrows(
            DuplicateContactException.class,
            () -> uniqueContactList.setContact(ALICE, TestData.Valid.Contact.BOB)
        );
    }

    @Test
    public void remove_nullContact_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueContactList.remove(null));
    }

    @Test
    public void remove_contactDoesNotExist_throwsContactNotFoundException() {
        assertThrows(ContactNotFoundException.class, () -> uniqueContactList.remove(ALICE));
    }

    @Test
    public void remove_existingContact_removesContact() {
        uniqueContactList.add(ALICE);
        uniqueContactList.remove(ALICE);
        UniqueContactList expectedUniqueContactList = new UniqueContactList();
        assertEquals(expectedUniqueContactList, uniqueContactList);
    }

    @Test
    public void setContacts_list_overwritesList() {
        uniqueContactList.add(ALICE);
        List<Contact> contactList = Collections.singletonList(TestData.Valid.Contact.BOB);
        uniqueContactList.setContacts(contactList);
        UniqueContactList expectedUniqueContactList = new UniqueContactList();
        expectedUniqueContactList.add(TestData.Valid.Contact.BOB);
        assertEquals(expectedUniqueContactList, uniqueContactList);
    }

    @Test
    public void setContacts_listWithDuplicates_throwsException() {
        List<Contact> listWithDuplicateContacts = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicateContactException.class, () -> uniqueContactList.setContacts(listWithDuplicateContacts));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueContactList.asUnmodifiableObservableList().remove(0));
    }
}