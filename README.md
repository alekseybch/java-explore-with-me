# Pull-request feature_comments: https://github.com/alekseybch/java-explore-with-me/pull/4  
  
## Возможности дополнительной функциональности  
- PrivateCommentController  
-- Создание комментария к опубликованным событиям GET /users/{userId}/comments/{eventId}  
-- Редактирование комментария автором PATCH /users/:userId:/comments/:commentId  
-- Удаление комментария автором DELETE /users/:userId:/comments/:commentId  
- AdminCommentController  
-- Редактирование комментария администратором PATCH /admin/comments/:commentId  
-- Удаление комментария администратором DELETE /admin/comments/:commentId  
- PublicCommentController  
-- Получение комментария по идентификатору GET /comments/?commentId=:commentId  
-- Получение всех комментариев к событию GET /comments/{eventId}  
-- Поиск комментариев события по тексту GET /comments/{eventId}?text=:text  
-- Получение всех комментариев события пользователя GET /comments/{eventId}?user=:userId  
-- Cортировка комментариев по возрастанию или убыванию даты создания GET /comments/{eventId}?sort=:sort
  
## ER-диаграмма main-service  
![main-service.png](erdiagram%2Fmain-service.png)  
  
## ER-диаграмма stats-server  
![stats-server.png](erdiagram%2Fstats-server.png)