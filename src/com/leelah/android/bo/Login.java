package com.leelah.android.bo;

public class Login
{

  /**
   * Pour les objets metier, les attributs n'on pas de getter/setter, ils sont publics et on les manipule directement (meilleur perf pour l'embarquer)
   */

  public String login;

  public String password;

  public Login(String login, String password)
  {
    this.login = login;
    this.password = password;
  }

}
