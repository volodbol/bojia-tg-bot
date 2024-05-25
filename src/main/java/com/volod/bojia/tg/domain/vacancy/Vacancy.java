package com.volod.bojia.tg.domain.vacancy;

import java.time.Instant;
import java.util.Comparator;

public record Vacancy(
        String company,
        String title,
        String shortDetails,
        String description,
        String url,
        Instant published
) {
    public static final Comparator<Vacancy> NEWEST = Comparator.comparing(Vacancy::published).reversed();

    public static Vacancy testsHardcoded() {
        return new Vacancy(
                "Gransoft solutions",
                "Middle Java Developer",
                "Relocate, Poland, Ukraine, 2 years of experience",
                """
                        We invite a Middle Java Developer to join our team for long-term cooperation.
                        Gransoft develop an international health insurance portal.
                        Our project has become especially important at this difficult time for all of us.
                        ▪️Office Location: Uzhhorod or Gdansk
                        
                        Requirements:
                        - Experience with Java, Hibernate, Spring, MySQL 2+ years.
                        - An intermediate-level proficiency in English: reading and writing.
                        - Responsible attitude to the tasks and deadlines
                        - Work from office only
                        - Be prepared for rare business trips abroad
                        
                        We offer:
                        - Interesting project
                        - Creative and friendly atmosphere
                        - Our company policy is to welcome your ideas and suggestions.
                        - An international team of experienced Java developers
                        - Paid vacations and days off during national holidays
                        - Flexible work schedule
                        - 8-hour workday
                        - Office in Uzhhorod or Gdansk
                        - Paid English language courses.
                        - Our company provides a relocation assistance, expenses reimbursement and accommodation if required.
                        
                        More details could be given during an interview.
                        Please include your Telegram nickname in your CV.
                        """,
                "https://djinni.co/jobs/?all-keywords=middle+java&title_only=on&keywords=middle+java",
                Instant.parse("2024-05-19T12:13:00.000Z"));
    }

    public String getTrimmedDescription(int length) {
        return this.description.substring(0, Math.min(length, this.description.length()));
    }
}
