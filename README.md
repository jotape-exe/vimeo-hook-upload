# Passo a passo 

1. Crie sua conta na vimeo e gere seu token/chave
  - [Ver Documentação da VIMEO](https://developer.vimeo.com/)

## Após configurar seu app vimeo, voce está apto para fazer o upload dos videos

2. Adicione o token no seu ambiente via properties
    ex: 

  ```yaml
    spring:
      servlet:
        multipart:
          enabled: true
          max-file-size: 450MB
          max-request-size: 450MB
      application:
        name: hook-upload
    vimeo:
      token: ${VIMEO_TOKEN} // CONFIGURE AQUI
      url: https://api.vimeo.com/me/videos

   ```
### Fluxo de upload (WEBHOOK)

1. Monte o payload em formato *form-data*

 - [POST] / `http://localhost:8080/iza-webhook`

#### Estrutura do FORM-DATA
<table>
  <thead>
    <tr>
       <th>key</th>
       <th>type</th>
       <th>content-type</th>
       <th>valor</th>
    </tr>
  </thead>
   <tbody>
     <tr>
      <td>payload</td>
      <td>Text</td>
      <td>application/json</td>
      <td>
        {
            "upload": {
                "approach": "tus",
                "size": "2988113" 
            },
            "name": "Teste Video Upload - anybody, public",
            "description": "Descrição XPTO",
            "privacy": {
                "view": "anybody",
                "embed": "public"
            },
            "folder_uri": "https://vimeo.com/user/{id_do_usuario}/folder/{id_da_pasta}"
        }
    </td>
     </tr>
     <tr>
      <td>video</td>
      <td>File</td>
      <td></td>
      <td>video.mp4 (certifique-se se enviar o video)</td>
     </tr>
  </tbody>
</table>

Segue exemplo via POSTMAN
![image](https://github.com/user-attachments/assets/0d2d9a47-5183-4cff-8192-4c6793e762b2)

---

### Fluxo de upload (VIMEO)

1. Crie a referência do video na VIMEO
 - [POST] / `https://api.vimeo.com/me/videos`
```json
{
    "upload": {
        "approach": "tus",
        "size": "2988113" // Corresponde ao tamanho do video enviado em bytes
    },
    "name": "Teste Video Upload - anybody, public",
    "description": "Descrição XPTO",
    "privacy": {
        "view": "anybody",
        "embed": "public"
    },
    "folder_uri": "https://vimeo.com/user/{id_do_usuario}/folder/{id_da_pasta}"
}
```



1.1 Acessar o reponse e enviar o video, com base no link obtido
```
// Link disnivel aqui
response.upload.upload_link
```
 - [PATCH] / ` https://abc-files.tus.vimeo.com/files/exemplo/idexemploxpto`

   > Certifique-se de enviar o video usando o formato <i>binary</i>

