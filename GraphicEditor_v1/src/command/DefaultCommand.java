package command;

import dto.CommandDTO;

public class DefaultCommand implements Command<CommandDTO> {

	@Override
	public void execute(CommandDTO dto) {
		// 아무것도 하지않음
	}
}