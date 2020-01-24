package fr.d2factory.libraryapp.member.student;

;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */
public class StudentFirstYear extends Student {

	/**
     * The member should pay their books when they are returned to the library
     * @param numberOfDays the number of days they kept the book
     */
    @Override
    public void payBook(long numberOfDays) {
        long normalDays = Math.max(0, Math.min(30, numberOfDays)-15);
        long lateDays = Math.max(numberOfDays - 30, 0);

        float wallet = getWallet(); 
        setWallet(wallet - (normalDays * 0.10f + lateDays * 0.20f));
    }
}
