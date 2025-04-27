#!/bin/bash
# generate_index.sh cloc-report.json output.html

INPUT_JSON=$1
OUTPUT_HTML=$2

cat > "$OUTPUT_HTML" <<EOF
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>Статистика проекта</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <style>
    body { font-family: Arial, sans-serif; padding: 20px; }
    h1 { text-align: center; }
    canvas { max-width: 600px; margin: 20px auto; display: block; }
    table { width: 100%; border-collapse: collapse; margin-top: 40px; }
    th, td { border: 1px solid #ddd; padding: 8px; text-align: center; }
    th { background-color: #f2f2f2; }
  </style>
</head>
<body>
  <h1>Отчёт о строках кода (CLOC)</h1>
  <canvas id="clocChart"></canvas>
  <table id="clocTable">
    <thead>
      <tr><th>Язык</th><th>Файлы</th><th>Код</th><th>Комментарии</th><th>Пустые строки</th></tr>
    </thead>
    <tbody></tbody>
  </table>

  <script>
    const clocData =
EOF

jq . "$INPUT_JSON" >> "$OUTPUT_HTML"

cat >> "$OUTPUT_HTML" <<EOF
    ;

    const labels = [];
    const codeLines = [];
    const tableBody = document.getElementById("clocTable").querySelector("tbody");

    for (const [lang, stats] of Object.entries(clocData)) {
      if (lang === "header") continue;
      labels.push(lang);
      codeLines.push(stats.code);

      const row = tableBody.insertRow();
      row.insertCell(0).textContent = lang;
      row.insertCell(1).textContent = stats.nFiles;
      row.insertCell(2).textContent = stats.code;
      row.insertCell(3).textContent = stats.comment;
      row.insertCell(4).textContent = stats.blank;
    }

    new Chart(document.getElementById("clocChart"), {
      type: "doughnut",
      data: {
        labels: labels,
        datasets: [{
          data: codeLines,
          backgroundColor: labels.map(() => \`hsl(\${Math.random()*360}, 70%, 60%)\`)
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { position: "bottom" },
          title: { display: true, text: "Количество строк кода по языкам" }
        }
      }
    });
  </script>
</body>
</html>
EOF
