Feature: Login Process

  Scenario: Epias Admin Login
    * "loginUsername" elementine "test1" degerini yaz
    * "loginPassword" elementine "123456" degerini yaz
    * "loginButton" elementine tıkla
    * "adminText" elementinin bulunduğunu kontrol et

  Scenario: Epias Admin Logout
    * "loginUsername" elementine "test1" degerini yaz
    * "loginPassword" elementine "123456" degerini yaz
    * "loginButton" elementine tıkla
    * "adminText" elementinin bulunduğunu kontrol et
    * "adminText" elementine tıkla
    * "cikisButton" elementine tıkla
    * "loginLabel" elementinin bulunduğunu kontrol et

  Scenario: Hamburger Menu Label Kontrol
    * "loginUsername" elementine "test1" degerini yaz
    * "loginPassword" elementine "123456" degerini yaz
    * "loginButton" elementine tıkla
    * "adminText" elementinin bulunduğunu kontrol et
    * "hamburgerMenu" elementine tıkla
    * "hamburgerList" elementinin bulunduğunu kontrol et

  Scenario:  Excel Download Kontrol
    * "loginUsername" elementine "test1" degerini yaz
    * "loginPassword" elementine "123456" degerini yaz
    * "loginButton" elementine tıkla
    * "adminText" elementinin bulunduğunu kontrol et
    * "hamburgerMenu" elementine tıkla
    * "hamburgerList" elementinin bulunduğunu kontrol et
    * "hamburgerListFaturalama" elementine tıkla
    * "hamburgerListFaturalamaAltLabel" elementine tıkla
    * "DownloadButton" elementine tıkla
    * 5 saniye bekle
    * Excel dosya indirme kontrolu


  #concept gibi kullanmak amacıyla login alanı için method yazımı


