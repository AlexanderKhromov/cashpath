const buyBtn = document.getElementById('buyButton');
const endTurnBtn = document.getElementById('endTurnButton');
const spinner = document.getElementById('spinner');

function updateGame(data) {
    console.log("MoveResponseDTO:", data);

    // update current player name
    document.getElementById("activePlayerName").innerText = data.currentPlayer.name;

    // updating data for all players
    data.players.forEach(player => {
        const pid = player.id;

        // update cash and daily cash flow
        const cashEl = document.getElementById("cash_" + pid);
        const flowEl = document.getElementById("dailyCashFlow_" + pid);
        const passiveEl = document.getElementById("passiveIncome_" + pid);

        if (cashEl) cashEl.innerText = player.cash.toFixed(2);
        if (flowEl) flowEl.innerText = player.dailyCashFlow.toFixed(2);
        if (passiveEl) passiveEl.innerText = player.passiveIncome.toFixed(2);

        // -------------------------------
        // 🔥 Update table liabilities
        // -------------------------------
        const liabilitiesBody = document.getElementById("liabilities_body_" + pid);
        if (liabilitiesBody) {
            liabilitiesBody.innerHTML = "";

            player.liabilities.forEach(l => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${l.name}</td>
                    <td>${l.dailyPayment.toFixed(2)}</td>
                `;
                liabilitiesBody.appendChild(row);
            });
        }

        // ------------------------------
        // 🔥 Update table assets
        // ------------------------------
        const assetsBody = document.getElementById("assets_body_" + pid);
        if (assetsBody) {
            assetsBody.innerHTML = "";

            player.assets.forEach(a => {
                const row = document.createElement("tr");
                row.innerHTML = `
                    <td>${a.name}</td>
                    <td>${a.dailyCashFlow.toFixed(2)}</td>
                `;
                assetsBody.appendChild(row);
            });
        }
    });

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