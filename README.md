#### TODO

TECH STACK
- spring boot, spring security, spring data jpa, postgresql, redis, jwt, flyway, elasticsearch, logstash, kibana, kafka, docker, kubernetes, twillio(sms,email?(alt. mailgun))

**AUTH**  
* [ ] POST /auth/login - kullanici girisi ve token olusturma
* [ ] POST /auth/register - yeni kullanici kaydi
* [ ] POST /auth/logout - guvenli cikis ve token iptal etme
* [ ] POST /auth/refresh - access token'i yenileme
* [ ] GET /auth/verify-token - token geçerliliğini kontrol etme
* [ ] POST /auth/activate-account - hesap aktivasyonu
* [ ] POST /auth/deactivate-account - hesabi devre disi birakma
* [ ] GET /auth/verify-email - e-posta dogrulama
* [ ] POST /auth/resend-verification - dogrulama e-postasini yeniden gonderme
* [ ] POST /auth/forgot-password - sifre sifirlama bağlantisi isteme
* [ ] POST /auth/reset-password - sifre sifirlama
* [ ] PUT /auth/change-password - oturum açmis kullanici için sifre degistirme
* [ ] GET /auth/sessions - aktif oturumlari listeleme
* [ ] DELETE /auth/sessions/{id}/revoke - nelirli bir oturumu sonlandirma
* [ ] DELETE /auth/sessions/revoke-all - tüm oturumlari sonlandirma

**USER**
* [ ] GET /api/users - tüm kullanicilari listele (sayfalama ve filtreleme ile)
* [ ] GET /api/users/{id} - id'ye göre kullanici getir
* [ ] GET /api/users/me - mevcut kullanici profilini getir
* [ ] PUT /api/users/{id} - kullanici bilgilerini güncelle
* [ ] PATCH /api/users/{id} - kullaniciyi pasif yap
* [ ] PUT /api/users/me - Mmevcut kullanici profilini güncelle
* [ ] GET /api/users/search - kriterlere göre kullanici ara
* [ ] GET /api/users/by-username/{username} - kullanici adina göre kullanici bul
* [ ] GET /api/users/by-email/{email} - e-postaya göre kullanici bul
* [ ] GET /api/users/{id}/roles - kullanicinin rollerini getir
* [ ] POST /api/users/{id}/roles/{roleid} - kullaniciya rol ata
* [ ] DELETE /api/users/{id}/roles/{roleid} - kullanicidan rol kaldir
* [ ] PUT /api/users/{id}/status - kullaniciyi aktiflestir/devre disi birak
* [ ] PUT /api/users/me/password - mevcut kullanicinin sifresini değistir
* [ ] PUT /api/users/{id}/password - admin kullanici sifresini sifirla
* [ ] PUT /api/users/me/email - mevcut kullanicinin e-postasini değistir
* [ ] PUT /api/users/me/preferences - kullanici tercihlerini güncelle
* [ ] PUT /api/users/me/profile-picture - profil resmini güncelle

**Rol Yönetimi**
* [ ] POST /api/roles - yeni rol olusturma
* [ ] GET /api/roles - tüm rolleri listeleme
* [ ] GET /api/roles/{id} - id'ye göre rol getirme
* [ ] PUT /api/roles/{id} - rol güncelleme
* [ ] DELETE /api/roles/{id} - rol silme
* [ ] GET /api/permissions - tüm izinleri listeleme
* [ ] POST /api/roles/{roleid}/permissions - role izin ekleme
* [ ] DELETE /api/roles/{roleid}/permissions/{permissionid} - rolden izin çikarma
* [ ] GET /api/roles/{roleid}/permissions - rolün tüm izinlerini getirme
* [ ] GET /api/permissions/{permissionid}/roles - izne sahip tüm rolleri getirme
* [ ] GET /api/roles/search - rolleri isim veya diğer kriterlere göre arama
* [ ] GET /api/roles/check-permission - belirli bir rolün belirli bir izne sahip olup olmadiğini kontrol etme

* [ ] POST /notifications/send - bildirim gönderme
* [ ] GET /notifications/settings - bildirim ayarlarini alma
* [ ] GET /notifications/templates - bildirim sablonlarini listeleme - email sablonlari, sms sblonlari vs.
* [ ] GET /notifications/history - bildirim geçmisini listeleme 
* [ ] GET /system/audit-log - sistem denetim günlüklerini listeleme
* [ ] POST /system/audit-log/search - denetim günlüklerini arama
* [ ] GET /system/user-activity - kullanici etkinliklerini izleme
* [ ] GET /system/login-history - giris geçmisini görüntüleme
----
* detaylarini sonra maddelerin altinda ayrintila
* suanda her kullanicinin bir rolu olacak sekilde ayarlandi. Buna gore gerekli kirilimlari yap
* Sadece bir tane sistem admini olacak. Bu admin tum kullanicilari gorebilecek ve yonetebilecek. Sadece bir ip uzerinden girisi yapabilecek. 
* permissionlar onceden olusturulacak ve baslangicta belirli rollere atanacak. 
* daha sonrasinda bu roller ile kullanicilara atanacak.
* adminler user create edebilir
* Kullanici kaydi silinmez, sadece devre disi birakilir.