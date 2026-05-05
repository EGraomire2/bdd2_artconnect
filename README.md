# ArtConnect Pro - Local Art Community Platform

## Overview
ArtConnect Pro is a JavaFX-based management system for local art communities. It allows managing artists, artworks, exhibitions, galleries, workshops, and community members.

This project is a skeleton designed for students to practice:
1. **Layered Architecture**: Presentation, Service, DAO, and Model layers.
2. **Database Persistence**: Implementing JDBC DAOs to connect to a MySQL database.
3. **JavaFX UI**: Working with FXML, TableViews, and Controllers.

## Project Structure
- `com.project.artconnect.MainApp`: Entry point.
- `com.project.artconnect.model`: Domain entities (POJOs/Stubs).
- `com.project.artconnect.dao`: Data Access Object interfaces.
- `com.project.artconnect.persistence`: JDBC implementations (TODO: Students implement these).
- `com.project.artconnect.service`: Business logic layer.
- `com.project.artconnect.ui`: JavaFX Controllers and FXML views.
- `com.project.artconnect.util`: Utility classes like `ConnectionManager` and `ServiceProvider`.

## How to Run
Requirement: Java 17+ and Maven installed.

```bash
mvn clean javafx:run
```

The application runs "out-of-the-box" using **In-Memory Services** (`InMemoryArtistService`, etc.) located in `com.project.artconnect.service.impl`. This allows immediate demonstration of the UI with dummy data.

## OOP-First Design (Object-Oriented Programming)
Unlike typical database-centric skeletons, ArtConnect Pro follows strict OOP best practices:
- **No Explicit IDs**: Model classes (`Artist`, `Artwork`, etc.) do **not** have `id` fields. In Java, an object's identity is its memory address/reference, not a numeric ID.
- **Direct Object References**: Relationships are modeled using direct references. For example, an `Artwork` object holds a reference to an `Artist` object, not an `artistId`.
- **Bidirectional Links**: Many relationships are bidirectional (e.g., an `Artist` has a `List<Artwork>`, and each `Artwork` points back to its `Artist`).
- **No Junction Tables**: Many-to-Many relationships (like Exhibitions and Artworks) are modeled using simple collections (`List<Artwork>`) rather than separate junction classes.

## Student Tasks (The Challenge)
1. **ID Discovery**: Students must "discover" or create IDs at the database level. Your JDBC DAOs will need to map database IDs (Primary Keys) to Java object references during the `findAll` or `save` operations.
2. **Relational Mapping**: You must implement the logic to reconstruct the object graph from relational tables. When fetching an `Artwork`, you must also fetch/link the corresponding `Artist`.
3. **Database Setup**: Create the MySQL database and tables as per the technical requirements (including IDs and Foreign Keys that are NOT visible in the Java models).
4. **JDBC Implementation**: Implement the `Jdbc` DAO classes in `com.project.artconnect.persistence`.
5. **Service Swap**: Update `ServiceProvider` to use your new `Jdbc` DAOs.

## Architecture Diagram
```mermaid
graph TD
    UI[JavaFX Presentation Layer] --> Service[Service Layer]
    Service --> DAO[DAO Interfaces]
    DAO --> JDBC[JDBC Persistence Implementation]
    DAO --> InMemory[InMemory Mock Implementation]
    JDBC --> DB[(MySQL Database)]
```

## Prototype de diagramme de classes
```mermaid
classDiagram
    class Artist {
        String name
        String bio
        Integer birthYear
        String contactEmail
        String phone
        String city
        String website
        String socialMedia
        boolean isActive
        List~Discipline~ disciplines
        List~Artwork~ artworks
        +addArtwork(Artwork artwork)
    }

    class Artwork {
        String title
        Integer creationYear
        String type
        String medium
        String dimensions
        String description
        double price
        Status status
        Artist artist
        List~ArtworkTag~ tags
    }

    class CommunityMember {
        String name
        String email
        Integer birthYear
        String phone
        String city
        String membershipType
        List~Discipline~ favoriteDisciplines
        List~Booking~ bookings
        List~Review~ reviews
        +addBooking(Booking booking)
    }

    class Workshop {
        String title
        LocalDateTime date
        int durationMinutes
        int maxParticipants
        double price
        Artist instructor
        String location
        String description
        String level
    }

    class Booking {
        Workshop workshop
        CommunityMember member
        LocalDateTime bookingDate
        String paymentStatus
    }

    class Review {
        CommunityMember reviewer
        Artwork artwork
        int rating
        String comment
        LocalDate reviewDate
    }

    class Gallery {
        String name
        String address
        String ownerName
        String openingHours
        String contactPhone
        double rating
        String website
        List~Exhibition~ exhibitions
        +addExhibition(Exhibition exhibition)
    }

    class Exhibition {
        String title
        LocalDate startDate
        LocalDate endDate
        String description
        Gallery gallery
        String curatorName
        String theme
        List~Artwork~ artworks
    }

    class Discipline {
        String name
    }

    class ArtworkTag {
        String name
    }

    class Status {
        <<enumeration>>
        FOR_SALE
        SOLD
        EXHIBITED
    }

    Artist "1" -- "0..*" Artwork : creates
    Artist "1" o-- "0..*" Discipline : disciplines
    Artwork "1" o-- "0..*" ArtworkTag : tags
    Artwork --> Status : status
    Workshop "0..*" --> "1" Artist : instructor
    CommunityMember "1" o-- "0..*" Booking : bookings
    CommunityMember "1" o-- "0..*" Review : reviews
    CommunityMember "0..*" o-- "0..*" Discipline : favoriteDisciplines
    Booking "1" --> "1" Workshop
    Booking "1" --> "1" CommunityMember : member
    Review "1" --> "1" Artwork
    Review "1" --> "1" CommunityMember : reviewer
    Gallery "1" o-- "0..*" Exhibition : exhibitions
    Exhibition "1" o-- "0..*" Artwork : artworks
    Exhibition "0..*" --> "1" Gallery : gallery
```

## Testing Instructions
1. Launch the app and verify all 7 tabs show dummy data.
2. Search for an artist by name or filter by discipline in the Artists Tab.
3. View the "Discover" tab to see featured content dynamically generated.
4. Once you implement JDBC, swap the `ServiceProvider` to use your `JdbcArtistDao` and verify data is fetched from MySQL.
