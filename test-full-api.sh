#!/bin/bash

echo "========================================="
echo "🧪 TESTE COMPLETO DA API"
echo "========================================="

# 1. Login Admin
echo -e "\n👑 1. Login Admin"
ADMIN_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}')
ADMIN_TOKEN=$(echo $ADMIN_RESPONSE | jq -r '.token')
echo "Admin Token: ${ADMIN_TOKEN:0:50}..."

# 2. Login User
echo -e "\n👤 2. Login User"
USER_RESPONSE=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user","password":"user123"}')
USER_TOKEN=$(echo $USER_RESPONSE | jq -r '.token')
echo "User Token: ${USER_TOKEN:0:50}..."

# 3. Listar usuários com Admin (deve funcionar)
echo -e "\n👑 3. Listar usuários (Admin)"
curl -s -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer $ADMIN_TOKEN" | jq '.[] | {id, username, email, role}'

# 4. Tentar listar usuários com User (deve falhar)
echo -e "\n❌ 4. Tentar listar usuários (User - deve falhar)"
HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X GET http://localhost:8080/api/admin/users \
  -H "Authorization: Bearer $USER_TOKEN")
if [ "$HTTP_CODE" == "403" ]; then
    echo "✅ Acesso negado (HTTP 403) - Correto!"
else
    echo "❌ Erro: Esperado 403, recebido $HTTP_CODE"
fi

# 5. Registrar novo usuário
echo -e "\n📝 5. Registrar novo usuário"
curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"testeapi","email":"testeapi@email.com","password":"Test@123"}' | jq .

echo -e "\n========================================="
echo "✅ TESTES CONCLUÍDOS!"
echo "========================================="
