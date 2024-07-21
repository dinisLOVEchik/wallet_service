package ru.nerzhaveyka.wallet;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.nerzhaveyka.wallet.controllers.MainController;
import ru.nerzhaveyka.wallet.entities.Wallet;
import ru.nerzhaveyka.wallet.servicies.WalletService;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class WalletApplicationTests {
	@Mock
	private WalletService walletService;

	@InjectMocks
	private MainController controller;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp(){
		mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testCreateWallet() throws Exception {
		mockMvc.perform(get("/api/v1/wallet/")).andExpect(status().isOk());
	}

	@Test
	public void testGetWalletById() throws Exception {
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		wallet.setBalance(100);
		when(walletService.getWalletById(1L)).thenReturn(wallet);
		mockMvc.perform(get("/api/v1/wallet/1"))
				.andExpect(status().isOk())
				.andExpect((ResultMatcher) content().json("{\"id\":1,\"balance\":100}"));
	}
	@Test
	public void testGetWalletBalance() throws Exception {
		Wallet wallet = new Wallet();
		wallet.setId(1L);
		wallet.setBalance(100);
		when(walletService.getWalletByWalletId(UUID.fromString("123e4567-e89b-12d3-a456-426655440000"))).thenReturn(wallet);
		mockMvc.perform(get("/api/v1/wallet/balance/123e4567-e89b-12d3-a456-426655440000").accept(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(status().isOk())
				.andExpect(content().string("Баланс кошелька 123e4567-e89b-12d3-a456-426655440000 равен: 100.0"));
	}
	@Test
	public void testOperationWithBalanceOfWallet_Deposit() throws Exception {
		WalletOperationRequestDto requestDto = new WalletOperationRequestDto(UUID.fromString("123e4567-e89b-12d3-a456-426655440000"), OperationTypes.DEPOSIT, 100);
		mockMvc.perform(post("/api/v1/wallet/operation").accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(requestDto)))
				.andExpect(status().isOk())
				.andExpect(content().string("Операция по изменению баланса прошла успешно"));
	}
	@Test
	public void testOperationWithBalanceOfWallet_Withdraw() throws Exception {
		WalletOperationRequestDto requestDto = new WalletOperationRequestDto(UUID.fromString("123e4567-e89b-12d3-a456-426655440000"), OperationTypes.WITHDRAW, 50);
		mockMvc.perform(post("/api/v1/wallet/operation").accept(MediaType.APPLICATION_JSON_UTF8)
						.contentType(MediaType.APPLICATION_JSON)
						.content(asJsonString(requestDto)))
				.andExpect(status().isOk())
				.andExpect(content().string("Операция по изменению баланса прошла успешно"));
	}
	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

