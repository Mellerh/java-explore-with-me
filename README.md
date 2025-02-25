# Explore-with-me

> ExploreWithMe (EWM) - сервис позволяет пользователям делиться информацией об интересных событиях и находить компанию для участия в них.
> Данное приложение можно представить в виде афиши. Только в этой афише пользователи могут не только рассказать про какое-либо событие (например, про фотовыставку или про премьеру в театре), но  и собрать компанию для участия в нём.

## Стек технологий
- Java
- Spring Boot
- Spring Data JPA / Hibernate
- Mockito
- Apache Maven
- Docker
- Swagger
- Lombok (+ SLF4J)
- Postman

## Функциональность приложения
EWM - имеет микросервисную архитектуру. Состоит из 2-х сервисов:
1. **main-service** - основной сервис содержит всё необходимое для функционирования приложения
2. **stats** - сервис статистики хранит количество просмотров каждого события

API основного сервиса разделен на три части:
- **публичная** (pub) доступна без регистрации любому пользователю сети:
    - просмотр события с подробной информацией
    - поиск и фильтрация событий по количеству просмотров, либо по датам событий
- **закрытая** (priv) доступна только авторизованным пользователям:
    - создание новых события и их редактирование
    - подача заявок на участие в мероприятиях
    - согласование заявок создателем мероприятия
- **административная** (admin) — для администраторов сервиса:
    - создание, редактирование и удаление категорий событий
    - создание и удаление подборки событий
    - удаление или редактирование событий
    - управление пользователями


## Описание API
<details>
  <summary><h3>Pubic</h3></summary>

- **GET** /compilations - просмотр подборок событий
- **GET** /compilations/{compId} - просмотр подборки событий по compId
- **GET** /categories - просмотр всех категорий
- **GET** /categories/{catId} - просмотр категории по catId
- **GET** /events - просмотр события с возможностью фильтрации
- **GET** /events/{id} - просмотр события по id
- **GET** /events/{id}/comments - просмотр комментариев к событию с id
</details>
<details>
  <summary><h3>Private</h3></summary>

- **GET** /users/{userId}/events - просмотр событий пользователя с userId
- **POST** /users/{userId}/events - добавление нового события пользователем с userId
- **GET** /users/{userId}/events/{eventId} - просмотр полной информации о событии с eventId, созданного пользователем с userId
- **PATCH** /users/{userId}/events/{eventId} - изменения события с eventId, созданного пользователем с userId
- **GET** /users/{userId}/events/{eventId}/requests - просмотр информации о заявках на участие в событии с eventId, созданного пользователем с userId
- **PATCH** /users/{userId}/events/{eventId}/requests - изменение статуса заявки на участие в событии с eventId, созданного пользователем с userId
- **GET** /users/{userId}/requests - просмотр всех заявок пользователя с userId
- **POST** /users/{userId}/requests - создание заявки на участие в событии пользователем с userId
- **PATCH** /users/{userId}/requests/{requestId}/cancel - отмена заявки с requestId пользователем с userId
- **POST** /users/{userId}/comments - создание комментария пользователем с userId
- **DELETE** /users/{userId}/comments/{commentId} - удаление комментария пользователем с userId
- **PATCH** /users/{userId}/comments - изменение комментария пользователем с userId
</details>
<details> 
 <summary><h3>Admin</h3></summary>

- **GET** /admin/events - поиск события
- **PATCH** /admin/events/{eventId} - редактирование события с eventId
- **DELETE** /admin/events/{commentId} - удаление комментария
- **POST** /admin/categories - создание новой категории
- **DELETE** /admin/categories/{catId} - удаление категории с catId
- **PATCH** /admin/categories/{catId} - изменение категории с catId
- **POST** /admin/compilations - создание новой подборки
- **DELETE** /admin/compilations/{compId} - удаление подборки с compId
- **PATCH** /admin/compilations/{compId} - изменение подборки с compId
- **GET** /admin/users - просмотр инфо о пользователях
- **POST** /admin/users - создание нового пользователя
- **DELETE** /admin/users/{userId} - удаление пользователя с userId
</details>

## Сборка и установка
Требования:
- Git
- JDK 11 или выше
- Maven 3.6.0 или выше
- Docker

Как запустить приложение:
1. Склонируйте репозиторий на локальную компьютер:
```bash
https://github.com/Anarmard/java-explore-with-me.git
```
2. Перейдите в директорию проекта:
```bash
cd java-explore-with-me
```
3. Соберите проект:
```bash
mvn clean install
```
4. Запустите приложение:
```bash
docker-compose up
```




