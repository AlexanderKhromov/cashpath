const buyBtn = document.getElementById('buyButton');
const endTurnBtn = document.getElementById('endTurnButton');
const spinner = document.getElementById('spinner');

function updateGame(data) {
    console.log("MoveResponseDTO:", data);
    const playerId = data.currentPlayer.id;

    document.getElementById("activePlayerName").innerText = data.currentPlayer.name;
    document.getElementById("cash_" + playerId).innerText = data.currentPlayer.cash.toFixed(2);
    document.getElementById("dailyCashFlow_" + playerId).innerText = data.currentPlayer.dailyCashFlow.toFixed(2);

    const card = data.card;
    if (card) {
        document.getElementById("cardDescription").innerText = card.description;
        document.getElementById("cardPrice").innerText = card.price.toFixed(2);
        cardId = card.id;
    }

    if (data.finished) {
        alert("Игра окончена! Победитель: " + data.winner);
        buyBtn.disabled = true;
        endTurnBtn.disabled = true;
    }
}

// Пример вызова API
async function buyCard() {
    spinner.style.display = 'block';
    buyBtn.disabled = true;
    endTurnBtn.disabled = true;

    try {
        const res = await fetch(`/api/games/${GAME_ID}/buy`, {
            method: 'POST',
            headers: {'Content-Type': 'application/json'},
            body: JSON.stringify({ cardId: cardId })
        });
        if (!res.ok) throw new Error('Ошибка запроса');
        const data = await res.json();
        updateGame(data);
    } finally {
        spinner.style.display = 'none';
        buyBtn.disabled = false;
        endTurnBtn.disabled = false;
    }
}

buyBtn.addEventListener('click', buyCard);
endTurnBtn.addEventListener('click', async () => {
    spinner.style.display = 'block';
    buyBtn.disabled = true;
    endTurnBtn.disabled = true;

    try {
        const res = await fetch(`/api/games/${GAME_ID}/end-turn`, { method: 'POST' });
        if (!res.ok) throw new Error('Ошибка запроса');
        const data = await res.json();
        updateGame(data);
    } finally {
        spinner.style.display = 'none';
        buyBtn.disabled = false;
        endTurnBtn.disabled = false;
    }
});