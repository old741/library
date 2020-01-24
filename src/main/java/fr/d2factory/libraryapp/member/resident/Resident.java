package fr.d2factory.libraryapp.member.resident;

import fr.d2factory.libraryapp.member.Member;

/**
 * A member is a person who can borrow and return books to a {@link Library}
 * A member can be either a student or a resident
 */

public class Resident extends Member {
	
    /**
     * The member should pay their books when they are returned to the library
     *
     * @param numberOfDays the number of days they kept the book
     */
	@Override
    public void payBook(long numberOfDays) {
        long normalDays = Math.min(60, numberOfDays);
        long lateDays = Math.max(numberOfDays - 60, 0);

        float wallet = getWallet(); 
        setWallet(wallet - (normalDays * 0.10f + lateDays * 0.20f));
    }
}
