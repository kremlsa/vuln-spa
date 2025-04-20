MVC

- Компонентная структура

- Model – классы данных (Note, User)

- Controller – контроллеры, принимающие HTTP-запросы

- Service – бизнес-логика (например, управление заметками и авторизацией)

- Repository – хранилище в памяти


backend/
├── pom.xml
└── src/
└── main/
├── java/
│   └── com/example/notes/
│       ├── controller/
│       │   ├── AuthController.java
│       │   └── NoteController.java
│       ├── model/
│       │   ├── Note.java
│       │   └── User.java
│       ├── repository/
│       │   └── NoteRepository.java
│       ├── service/
│       │   ├── AuthService.java
│       │   └── NoteService.java
│       └── NotesApplication.java
└── resources/
└── application.properties

