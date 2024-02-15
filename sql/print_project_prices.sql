SELECT 
    c.name AS client_name,
    SUM(w.salary) * (DATEDIFF(MONTH, p.start_date, p.finish_date) + 1) AS project_price
FROM 
    project p
JOIN 
    project_worker pw ON p.id = pw.project_id
JOIN 
    worker w ON pw.worker_id = w.id
JOIN 
    client c ON p.client_id = c.id
GROUP BY 
    p.id, p.start_date, p.finish_date, c.name
ORDER BY 
    project_price DESC;