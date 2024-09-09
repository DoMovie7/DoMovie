SELECT id, author, content, created_at, updated_at
FROM comment
WHERE home_theater_id = ?
ORDER BY created_at DESC