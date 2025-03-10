import { useState } from "react";
import axios from "axios";

const Chatt = () => {
  const [messages, setMessages] = useState([]);
  const [message, setMessage] = useState("");
  const [loading, setLoading] = useState(false);

  const handleSend = async () => {
    if (!message.trim()) return;

    const userMessage = { text: message, sender: "user" };
    setMessages((prev) => [...prev, userMessage]);

    setLoading(true);
    try {
      const res = await axios.get("http://localhost:8080/ai/generate", {
        params: { message },
      });

      const aiMessage = { text: res.data.generation, sender: "ai" };
      setMessages((prev) => [...prev, aiMessage]);
    } catch (error) {
      setMessages((prev) => [
        ...prev,
        { text: "Error fetching response", sender: "ai" },
      ]);
    }
    setLoading(false);
    setMessage("");
  };

  return (
    <div className="chat-container">
      <h1 className="chat-title">DeepSeek AI Chatbot</h1>
      <div className="chat-box">
        {messages.map((msg, index) => (
          <div key={index} className={`chat-message ${msg.sender}`}>
            {msg.text}
          </div>
        ))}
        {loading && <div className="chat-message ai">Thinking...</div>}
      </div>
      <div className="chat-input-container">
        <textarea
          className="chat-input"
          value={message}
          onChange={(e) => setMessage(e.target.value)}
          placeholder="Type your message..."
        />
        <button className="chat-button" onClick={handleSend} disabled={loading}>
          Send
        </button>
      </div>
    </div>
  );
};

export default Chatt;
