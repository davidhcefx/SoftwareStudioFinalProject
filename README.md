# SoftwareStudioFinalProject
# 專案題目：Catch Me If You Can

小組成員：yalinyang, th3lusive, davidhcefx, miamialin

## 概述 (Introduction)：

從Agar.io得到靈感，一開始是想建立多人連線遊戲，並且將規則改變為循環的相互吞噬關係（也就是A>B>C，並且C>A）。
後來演變成單機三人遊戲，並且在地圖中加入了迷宮的概念，以及道具，讓整個遊戲兼具互相追逐較勁、以及迷宮解謎的雙重趣味。
我們除了以上這些特色外，還定出了幾個Optional計畫，如果有時間的話也會實作（連線功能、滑鼠拖曳）。

## 設計概念 (Design Concept)：

- 迷宮設計：場地會是（較為簡單的）迷宮，提升相互追逐時的難度。

- 屬性相剋：種族A會剋種族B，種族B剋種族C，而種族C剋種族A。循環的天敵/獵物關係。
	
- 特殊道具：地圖上會隨機出現不同效果的道具（eg.方向錯亂、速度提升），每個道具都有時效性，得到立即作用，時效過了消失。若得到一個以上道具，效果會疊加。
		
- 關卡：越後面的關卡，道路會越窄（難以閃避道具），且負面道具變多。

	草圖：<p>
	![HandScratch](/res/readme/scratch.png)

## 實作計畫(Implementation Plan)：

- 開發工具：processing

- 開始畫面：Menu用processing GUI。Button 包含START, Help, 單機(2 AI)或三人版。

- 角色：剪刀，石頭和布

- 進入遊戲：角色隨機，介面上道具隨機分散在道路上

- 遊戲界面：最上方Status Bar會顯示A, B, C三種種族當下的相剋關係（因遊戲時可能會再換），以及各個道具的作用剩餘時間。

	示意圖：<p>
	![SimpleImage](/res/readme/image.gif)

- 關卡地圖：載入現成圖片，並同時用陣列表示來作運算

- 音樂(BGM)：背景音樂用processing的minim的java audio library
	
- 連接關卡：background會連接一animation顯示即將進入下一關，畫面結束後，即是下一關。
	
- End animation

## 時程預估(Timeline)：

- Week1:完成到三角色可以在迷宮裡動(LEVEL1)；判斷勝負；加BGM
- Week2:在迷宮裡加道具。開始，menu，轉場和結束畫面完成;決定是否要加optional design
- Week3:完成其他LEVEL。有optional就儘量完成。
- Week4:統整，大家彼此debug

## 工作分配：

|姓名|分配|
|:--- |:---|
|yalinyang|角色|
|th3lusive|主畫面|
|davidhcefx|開場，結束，轉場，menu|
|miamialin|道具|

## Optional design:

- [ ] 連線功能：如果可行，希望能完成在不同電腦上同步進行這遊戲(簡單來說，就是在不同電腦上玩)用一人電腦當server，其他是clients.
	
- [ ] 在迷宮上加金幣。可以在迷宮到路上撿金幣，並且可以用一定金幣來抵銷負面功能。點金幣圖示拖曳到其他負面道具上。



