#!/bin/bash
INPUT_JSON=$1
OUTPUT_HTML=$2

cat > "$OUTPUT_HTML" <<EOF
<!DOCTYPE html>
<html lang="ru">
<head>
  <meta charset="UTF-8">
  <title>SBOM Отчёт</title>
  <style>
    body { font-family: Arial, sans-serif; padding: 20px; }
    table { width: 100%; border-collapse: collapse; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 8px; }
    th { background-color: #f2f2f2; }
  </style>
</head>
<body>
  <h1>Отчёт об используемых компонентах (SBOM)</h1>
  <table id="sbomTable">
    <thead>
      <tr><th>Имя</th><th>Версия</th><th>Тип</th></tr>
    </thead>
    <tbody></tbody>
  </table>

  <script>
    const sbomData =
EOF

jq . "$INPUT_JSON" >> "$OUTPUT_HTML"

cat >> "$OUTPUT_HTML" <<EOF
    ;

    const tableBody = document.getElementById("sbomTable").querySelector("tbody");

    sbomData.components.forEach(component => {
      const row = tableBody.insertRow();
      row.insertCell(0).textContent = component.name;
      row.insertCell(1).textContent = component.version;
      row.insertCell(2).textContent = component.type;
    });
  </script>
</body>
</html>
EOF