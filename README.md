# Graph-Based Adventure Game

## Overview
This project is a graph-based, single- or multiplayer adventure game where players traverse a map filled with challenges and events to reach the goal. The game uses a graph structure of at least 50 vertices and 50 edges, applying traversal and pathfinding algorithms to create an engaging, strategic gameplay experience.

---

## Original Requirements
The project required designing, analyzing, and implementing a game that could be modeled using graphs. The game needed to include:
- A graph structure with at least 50 vertices and 50 edges.
- The use of at least two graph algorithms, such as:
  - **Traversals (BFS, DFS)**
  - **Shortest Path Algorithms (Dijkstra, Floyd-Warshall)**
  - **Minimum Spanning Trees (Prim, Kruskal)**

We designed a dynamic game that met these requirements while offering a fun and challenging player experience.

---

## Gameplay Description
Players explore a map where each vertex represents a location or event. Events include:
- **Battles with Enemies**: Players fight enemies with outcomes based on probabilities. 
- **Campfire Events**: Rest stops where players can recover or prepare for future encounters.
- **Treasure Hunts**: Collect items to boost scores or improve chances in battles.
- **Escape Options**: Players can flee battles at the cost of reducing their chances of success in future encounters.

### Victory and Scoring
- **Win**: Reach the opposite side of the map.
- **Lose**: Fail to survive the events along the way.
- **Scoring**: Players earn points for completing challenges, with harder paths offering greater rewards.

---

## Technical Highlights
- **Graph Representation**: The map is a graph where vertices are locations and edges are paths connecting them.
- **Algorithms Used**:
  1. **BFS (Breadth-First Search)**: Ensures connectivity of the map and links all vertices.
  2. **Dijkstra’s Algorithm**: Highlights the easiest path to victory, giving players a safer route to follow.
  3. **Custom "Hardest Path" Algorithm**: Combines elements of Dijkstra and BFS to calculate a challenging, high-reward route.
- **Event Randomization**: Events are procedurally generated to ensure replayability.
- **2D Interface**: A simple 2D interface visually represents the map, paths, and player progress.

---

## Acknowledgments
A big thank you to my team for their creativity and technical expertise in turning this concept into a reality. The combination of strategic design and algorithmic implementation made this project both challenging and rewarding.

---

## License
Feel free to use or adapt this project for educational or personal purposes. We hope it serves as inspiration for your own graph-based game ideas. Have fun exploring and playing!
