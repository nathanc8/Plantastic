import { useEffect, useState } from "react";
import "./App.css";
import type { User } from "./mocks/handlers";
import React from "react";

export function App() {
  const [users, setUsers] = useState<User[]>([]);
  const [pseudo, setPseudo] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  // GET REQUEST
  useEffect(() => {
    const fetchUsers = async () => {
      try {
        const response = await fetch("api/auth/login");
        const data: User[] = await response.json();
        setUsers(data);
      } catch (error) {
        console.debug("Erreur lors du fetch", error);
      }
    };
    fetchUsers();
  }, []);

  // POST REQUEST
  const onPostClick = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch("/api/auth/signup", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ pseudo, email, password }),
      });
      if (!response.ok) {
        console.debug("Erreur API:", response.status);
        return;
      }
      const newUser: User = await response.json();
      console.debug("New user created: ", newUser);

      setUsers((prev) => [...prev, newUser]);

      setPseudo("");
      setEmail("");
      setPassword("");
    } catch (error) {
      console.debug("Erreur lors du POST", error);
    }
  };

  return (
    <div>
      <h2>Existing users :</h2>
      {users.map((user) => (
        <div key={user.id}>
          {user.pseudo} - {user.email}
        </div>
      ))}
      <div>
        <h2>User created :</h2>
        <form onSubmit={onPostClick}>
          <input
            value={pseudo}
            onChange={(e) => {
              setPseudo(e.target.value);
            }}
          />
          <input
            value={email}
            onChange={(e) => {
              setEmail(e.target.value);
            }}
          />
          <input
            value={password}
            onChange={(e) => {
              setPassword(e.target.value);
            }}
          />
          <button type="submit">Send</button>
        </form>
      </div>
    </div>
  );
}
