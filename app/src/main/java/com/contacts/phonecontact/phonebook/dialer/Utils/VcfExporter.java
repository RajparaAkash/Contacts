package com.contacts.phonecontact.phonebook.dialer.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Contact;
import com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.PhoneNumber;
import com.contacts.phonecontact.phonebook.dialer.DataHelper.utils.ContactPhotoUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import ezvcard.Ezvcard;
//import ezvcard.VCard;
//import ezvcard.VCardVersion;
//import ezvcard.parameter.ImageType;
//import ezvcard.parameter.VCardParameters;
//import ezvcard.property.Email;
//import ezvcard.property.Organization;
//import ezvcard.property.Photo;
//import ezvcard.property.StructuredName;
//import ezvcard.property.Telephone;
//import ezvcard.property.Title;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class VcfExporter {
//    private String CELL = "CELL";
//    private String FAX = "FAX";
//    private String HOME = "HOME";
//    private String MAIN = "MAIN";
//    private String MOBILE = "MOBILE";
//    private String OTHER = "OTHER";
//    private String PAGER = "PAGER";
//    private String PREF = VCardParameters.PREF;
//    private String WORK = "WORK";
//    private int contactsExported;
//    private int contactsFailed;
//
//    public String getCELL() {
//        return this.CELL;
//    }
//
//    public String getWORK() {
//        return this.WORK;
//    }
//
//    public String getHOME() {
//        return this.HOME;
//    }
//
//    public String getOTHER() {
//        return this.OTHER;
//    }
//
//    public String getPREF() {
//        return this.PREF;
//    }
//
//    public String getMAIN() {
//        return this.MAIN;
//    }
//
//    public String getFAX() {
//        return this.FAX;
//    }
//
//    public String getPAGER() {
//        return this.PAGER;
//    }
//
//    public String getMOBILE() {
//        return this.MOBILE;
//    }
//
//    public void exportContacts(Activity activity, OutputStream outputStream, List<Contact> list, Function1<? super ExportResult, Unit> function1) {
//        ExportResult exportResult;
//        if (outputStream == null) {
//            try {
//                function1.invoke(ExportResult.EXPORT_FAIL);
//            } catch (Exception e) {
//                System.out.println((Object) ("error " + e));
//            }
//        } else {
//            ArrayList<VCard> arrayList = new ArrayList<>();
//            for (Contact contact : list) {
//                VCard vCard = new VCard();
//                StructuredName structuredName = new StructuredName();
//                structuredName.getPrefixes().add(contact.getNamePrefix());
//                structuredName.setGiven(contact.getFirstName());
//                structuredName.getAdditionalNames().add(contact.getMiddleName());
//                structuredName.setFamily(contact.getSurName());
//                structuredName.getSuffixes().add(contact.getNameSuffix());
//                vCard.setStructuredName(structuredName);
//                for (PhoneNumber t : contact.getContactNumber()) {
//                    Telephone telephone = new Telephone(t.getValue());
//                    telephone.getParameters().addType(getPhoneNumberTypeLabel(TypeGetterUtils.getOriginalType(t.getType()), t.getValue()));
//                    vCard.addTelephoneNumber(telephone);
//                }
//                for (com.contacts.phonecontact.phonebook.dialer.AllModels.ContactDataModels.Email t2 : contact.getContactEmail()) {
//                    Email email = new Email(t2.getValue());
//                    email.getParameters().addType(getEmailTypeLabel(TypeGetterUtils.getOriginalType(t2.getType()), t2.getLabel()));
//                    vCard.addEmail(email);
//                }
//                boolean z = false;
//                if (!contact.getContactNotes().isEmpty()) {
//                    for (String t3 : contact.getContactNotes()) {
//                        vCard.addNote(contact.getContactNotes().get(0));
//                    }
//                }
//                if (contact.getCompany().length() > 0) {
//                    Organization organization = new Organization();
//                    organization.getValues().add(contact.getCompany());
//                    vCard.setOrganization(organization);
//                    vCard.getTitles().add(new Title(contact.getJobPosition()));
//                }
//                Iterator<String> it = contact.getWebsites().iterator();
//                while (it.hasNext()) {
//                    vCard.addUrl((String) it.next());
//                }
//                String contactPhotoUri = contact.getContactPhotoUri();
//                if (contactPhotoUri == null || contactPhotoUri.length() == 0) {
//                    z = true;
//                }
//                if (!z) {
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), Uri.parse(contact.getContactPhotoUri()));
//                        vCard.addPhoto(new Photo(ContactPhotoUtils.getByteArray(bitmap), ImageType.JPEG));
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//                arrayList.add(vCard);
//                this.contactsExported++;
//            }
//            try {
//                Ezvcard.write(arrayList).version(VCardVersion.V4_0).go(outputStream);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            if (this.contactsExported == 0) {
//                exportResult = ExportResult.EXPORT_FAIL;
//            } else if (this.contactsFailed > 0) {
//                exportResult = ExportResult.EXPORT_PARTIAL;
//            } else {
//                exportResult = ExportResult.EXPORT_OK;
//            }
//            function1.invoke(exportResult);
//        }
//    }
//
//    private String getPhoneNumberTypeLabel(int i, String str) {
//        if (i == 12) {
//            return this.PREF;
//        }
//        switch (i) {
//            case 1:
//                return this.HOME;
//            case 2:
//                return this.CELL;
//            case 3:
//                return this.WORK;
//            case 4:
//                return "WORK;FAX";
//            case 5:
//                return "HOME;FAX";
//            case 6:
//                return this.PAGER;
//            case 7:
//                return this.OTHER;
//            default:
//                return str;
//        }
//    }
//
//    private String getEmailTypeLabel(int i, String str) {
//        if (i == 1) {
//            return this.HOME;
//        }
//        if (i == 2) {
//            return this.WORK;
//        }
//        if (i == 3) {
//            return this.OTHER;
//        }
//        if (i != 4) {
//            return str;
//        }
//        return this.MOBILE;
//    }
//
//    private String getAddressTypeLabel(int i, String str) {
//        if (i == 1) {
//            return this.HOME;
//        }
//        if (i == 2) {
//            return this.WORK;
//        }
//        if (i != 3) {
//            return str;
//        }
//        return this.OTHER;
//    }
//
//    public enum ExportResult {
//        EXPORT_FAIL,
//        EXPORT_OK,
//        EXPORT_PARTIAL
//    }
}
