package com.leelah.android.bo;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User
    implements Serializable
{

  private static final long serialVersionUID = -8479545031484426412L;

  /**
   * Pour les objets metier, les attributs n'on pas de getter/setter, ils sont publics et on les manipule directement (meilleur perf pour l'embarquer)
   * 
   * t.string "first_name", :limit => 25, :null => false t.string "last_name", :limit => 25, :null => false t.string "email", :limit => 64, :null =>
   * false t.string "login", :limit => 25, :null => false t.string "password", :null => false t.string "token", :null => false t.string "salt", :null
   * => false t.integer "status", :default => 0 t.string "reference_client", :limit => 25 t.datetime "created_at" t.datetime "updated_at"
   */

  public String first_name;

  public String last_name;

  public String email;

  public String login;

  public String password;

  public String token;

  @JsonIgnore
  public String address;

  public User()
  {

  }

  public User(String login, String password, String address)
  {
    this.login = login;
    this.password = password;
    this.address = address;
  }

}
