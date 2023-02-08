<h1 align="center">
  Seed.io 🌱
</h1>
<h3 align="center">
  An android mobile app that allow users to post and discuss ideas
</h3>

<img title="Mockup" src="https://user-images.githubusercontent.com/89917595/217586174-6d7e6442-3684-4ead-bb63-9e652fb7bd07.png" width=100%>


### 🤔 Problem
There is no easy way to interact between builders with projects, consumers & early adopters, and investors, among others. There is also no centralized way to read about new ideas and activities in space. Additionally, great ideas born from casual projects that may not yet be company material are discarded instead of published to get productive feedback, which could lead to a venture, or as means to inspire other ideas.

### 💡 Solution
“Twitter for projects” + “Figma for User Experience”  = Superapp for idea/early-stage project development 

A mobile app that allows builders to easily post their projects (like a Tweet) and get a thread running for feedback. Builders will also be able to create posts like “Check out this new feature,” “Bug hunt live in 10 minutes,” or “What cool features would you like to see”,  notifying all their following. There will also be an “Explore/Discover” page where users would be able to find cool trending projects in different industries. There would also be a feature that would allow someone who wants to get involved in the project (investor/adopter/developer/designer, etc.) to indicate their interest to the builder and they will be “matched” if the builder accepts. There will also be project founders’ profiles on the app and clicking on a post will give you more information (such as a link to their source code, etc.)

https://user-images.githubusercontent.com/89917595/217364502-e050b56d-341a-4086-94ee-7a9d3beac9ee.mp4

### 🪧 Architecture
<img width="779" alt="Screen Shot 2023-02-07 at 4 02 51 PM" src="https://user-images.githubusercontent.com/89917595/217364954-e9fff3f5-58ad-4a18-98d5-fa8add00d5bf.png">

### 🛠 Frontend (Screens)
1. Login & Sign-up Page
2. Profile Setup Page (for the first sign up)
3. Profile
4. Profile edit
5. Settings (log out and feedback)
6. Timeline
7. Post detail view
8. Post creation

### 🛠 Backend (Firebase Database)
<b>Post</b>
| Field          | Type           |
| -------------- | -------------- |
| Tag            | Short          |
| upvotes        | Int            |
| comments       | Int            |
| Author         | < User >       |
| Title          | String         |
| Body text      | String         |
| Timestamp      | Date           |

<b>User</b>
| Field             | Type           |
| ----------------- | -------------- |
| Username          | String         |
| GoogleID          | String         |
| Bio               | String         |
| Photo             | String(URL)    |

<b>Comment</b>
| Field          | Type           |
| -------------- | -------------- |
| PostID         | Int            |
| Author         | < User >       |
| Text           | String         |
| Timestamp      | Date           |

