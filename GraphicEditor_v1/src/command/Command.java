package command;

import dto.CommandDTO;

public interface Command<T extends CommandDTO> {
	void execute(T DTO);
}
